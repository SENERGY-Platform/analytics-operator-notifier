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


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Notifier {
    CloseableHttpClient httpClient;
    String notifierUrl;
    ResponseHandler responseHandler;

    public Notifier(String notifierUrl) {
        this.notifierUrl = notifierUrl;
        httpClient = HttpClientBuilder.create().build();
        responseHandler = response -> {
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            return null;
        } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }
};
    }

    public void createNotification(Notification n) throws IOException {
        HttpPost request = new HttpPost(notifierUrl + "/notifications");
        HttpEntity stringEntity = new ByteArrayEntity(n.toJSON().getBytes(StandardCharsets.UTF_8), ContentType.APPLICATION_JSON);
        request.setEntity(stringEntity);
        httpClient.execute(request, responseHandler);
    }
}



