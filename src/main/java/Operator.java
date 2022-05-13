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

import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.Config;
import org.infai.ses.senergy.operators.Stream;
import org.infai.ses.senergy.utils.ConfigProvider;
import notifier.Notifier;

public class Operator {

    public static void main(String[] args) {
        Config config = ConfigProvider.getConfig();

        String title = config.getConfigValue("title", "");
        String message = config.getConfigValue("message", "");
        Notifier notifier = new Notifier(config.getConfigValue("notifier-url", "http://api.notifier:5000"));

        BaseOperator op = new OperatorNotifier(notifier, title, message, config.getUserId());
        Stream stream  = new Stream();
        stream.start(op);
    }
}
