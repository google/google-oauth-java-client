/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.auth.oauth2;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.client.util.Key;
import junit.framework.TestCase;

/**
 * Tests {@link TokenResponse}.
 *
 * @author Jeff Ching
 */
public class CustomTokenResponseTest extends TestCase {

  private static final String JSON = "{\"access_token\":\"2YotnFZFEjr1zCsicMWpAA\","
      + "\"token_type\":\"example\",\"expires_in\":\"3600\","
      + "\"refresh_token\":\"tGzv3JOkF0XG5Qx2TlKWIA\","
      + "\"example_parameter\":\"example_value\"}";

  public static class StringExpiresTokenResponse extends TokenResponse {

    @Key("expires_in")
    private String expiresInSecondsString;

    public Long getExpiresInSeconds() {
      return Long.parseLong(expiresInSecondsString);
    }

    public StringExpiresTokenResponse setExpiresInSeconds(Long expiresInSeconds) {
      expiresInSecondsString = expiresInSeconds.toString();
      return this;
    }
  }

  public void testStringExpires() throws Exception {
    JsonFactory jsonFactory = new JacksonFactory();
    TokenResponse response = jsonFactory.fromString(JSON, StringExpiresTokenResponse.class);
    assertEquals("2YotnFZFEjr1zCsicMWpAA", response.getAccessToken());
    assertEquals("example", response.getTokenType());
    assertEquals(3600L, response.getExpiresInSeconds().longValue());
    assertEquals("tGzv3JOkF0XG5Qx2TlKWIA", response.getRefreshToken());
    assertEquals("example_value", response.get("example_parameter"));
  }
}
