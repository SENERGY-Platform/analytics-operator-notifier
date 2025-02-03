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
package notifier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Notification {
    private final String message, title, userId;
    private final boolean isRead;

    public Notification(String title, String message, String userId) {
        this.message = message;
        this.title = title;
        this.userId = userId;
        isRead = false;
    }

    public String toJSON() {
        return "{" +
                "\"created_at\":null," +
                "\"isRead\":" + isRead + "," +
                "\"title\":\"" + title + "\"," +
                "\"message\":\"" + message + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"topic\":\"analytics\"" + 
                "}";
    }
}
