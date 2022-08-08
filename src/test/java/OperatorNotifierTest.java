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

import notifier.Notifier;
import org.infai.ses.senergy.models.DeviceMessageModel;
import org.infai.ses.senergy.models.MessageModel;
import org.infai.ses.senergy.operators.BaseOperator;
import org.infai.ses.senergy.operators.Config;
import org.infai.ses.senergy.operators.Helper;
import org.infai.ses.senergy.operators.Message;
import org.infai.ses.senergy.testing.utils.JSONHelper;
import org.infai.ses.senergy.utils.ConfigProvider;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.junit.Test;

public class OperatorNotifierTest {


    public void run(Config config) {
        JSONArray messages = new JSONHelper().parseFile("sample-data-small.json");

        String topicName = config.getInputTopicsConfigs().get(0).getName();
        ConfigProvider.setConfig(config);
        Message message = new Message();
        MessageModel model = new MessageModel();
        String title = config.getConfigValue("title", "");
        String messageConfig = config.getConfigValue("message", "");
        Notifier notifier = new Notifier("http://localhost:5000");
        BaseOperator op = new OperatorNotifier(notifier, title, messageConfig, config.getUserId());
        op.configMessage(message);

        for (Object m : messages) {
            DeviceMessageModel deviceMessageModel = JSONHelper.getObjectFromJSONString(m.toString(), DeviceMessageModel.class);
            assert deviceMessageModel != null;
            model.putMessage(topicName, Helper.deviceToInputMessageModel(deviceMessageModel, topicName));
            message.setMessage(model);
            try {
                op.run(message);
            } catch (Exception e) {
                System.err.println("Running message failed. This might be okay, if integration is not tested.");
            }
        }
    }

    @Test
    public void test() throws Exception {
        JSONObject jsonConfig = new JSONObject(getConfig());
        run(new Config(jsonConfig.toString()));
    }


    private static String getConfig() {
        return "{\n" +
                "  \"inputTopics\": [\n" +
                "    {\n" +
                "      \"name\": \"iot_bc59400c-405c-4c84-9862-a791daa82b60\",\n" +
                "      \"filterType\": \"DeviceId\",\n" +
                "      \"filterValue\": \"0\",\n" +
                "      \"mappings\": [\n" +
                "        {\n" +
                "          \"dest\": \"value\",\n" +
                "          \"source\": \"value.reading.timestamp\"\n" +
                "        }" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "\"config\": {\"title\":\"someTitle äöüß %s\",\"message\":\"just a msg\"}\n" +
                "}";
    }
}
