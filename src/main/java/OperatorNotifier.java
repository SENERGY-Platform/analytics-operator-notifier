/*
 * Copyright 2022 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import notifier.Notification;
import notifier.Notifier;
import org.infai.ses.senergy.exceptions.NoValueException;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.Message;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OperatorNotifier extends BaseOperator {
    private final Notifier notifier;
    private final String title, message, userId;
    private final int ignoreDuplicatesWithinSeconds;

    private LocalDateTime lastNotification;
    private Object lastMessage;

    private static final Logger log = LoggerFactory.getLogger(OperatorNotifier.class);

    public OperatorNotifier(Notifier notifier, String title, String message, String userId, int ignoreDuplicatesWithinSeconds) {
        this.notifier = notifier;
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.ignoreDuplicatesWithinSeconds = ignoreDuplicatesWithinSeconds;
        this.lastNotification = LocalDateTime.now();
        this.lastMessage = "";
    }

    @Override
    public void run(Message m) {
        try {
            Object in = m.getFlexInput("value").getValue(Object.class);
            String fTitle = String.format(title, in);
            String fMessage = String.format(message, in);
            log.debug("Creating notification:\n\ttitle: {}\n\tmessage: {}", fTitle, fMessage);
            final LocalDateTime presentTime = LocalDateTime.now();
            final double timeSinceLastNotification = Duration.between(presentTime, lastNotification).abs().get(ChronoUnit.SECONDS);
            if (!in.equals(lastMessage) || timeSinceLastNotification > ignoreDuplicatesWithinSeconds) {
                Notification n = new Notification(fTitle, fMessage, userId);
                notifier.createNotification(n);
                lastNotification = presentTime;
            }
            lastMessage = in;
        } catch (NoValueException e) {
            log.error("Error getting a value, skipping message....", e);
        } catch (IOException e) {
            log.error("Error creating a notification, failing hard!", e);
            throw new RuntimeException();
        }
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        return  message;
    }
}
