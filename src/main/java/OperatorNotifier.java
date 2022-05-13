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
import org.infai.ses.senergy.operators.Helper;
import org.infai.ses.senergy.operators.Message;

import java.io.IOException;

public class OperatorNotifier extends BaseOperator {
    private final Notifier notifier;
    private final String title, message, userId;
    private final boolean DEBUG;

    public OperatorNotifier(Notifier notifier, String title, String message, String userId) {
        this.notifier = notifier;
        this.title = title;
        this.message = message;
        DEBUG = Boolean.parseBoolean(Helper.getEnv("DEBUG", "false"));
        this.userId = userId;
    }

    @Override
    public void run(Message m) {
        try {
            Object in = m.getFlexInput("value").getValue(Object.class);
            String fTitle = String.format(title, in);
            String fMessage = String.format(message, in);
            if (DEBUG) {
                System.out.println("Creating notification:\n\ttitle: " + fTitle + "\n\tmessage: " + fMessage);
            }
            Notification n = new Notification(fTitle, fMessage, userId);
            notifier.createNotification(n);
        } catch (NoValueException e) {
            System.err.println("Error getting a value, skipping message....");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error creating a notification, failing hard!");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public Message configMessage(Message message) {
        message.addFlexInput("value");
        return  message;
    }
}
