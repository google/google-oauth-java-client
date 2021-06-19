/*
 * Copyright 2021 Google LLC
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

package com.google.api.client.auth.oauth;

import com.google.api.client.util.Base64;
import com.google.api.client.util.Beta;
import com.google.api.client.util.StringUtils;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * {@link Beta} <br>
 * OAuth {@code "HMAC-SHA256"} signature method.
 */
@Beta
public final class OAuthHmacSha256Signer implements OAuthSigner {

  /** Client secret */
  private String clientSharedSecret;

  /** Token secret */
  private String tokenSharedSecret;

  public void setClientSecret(String clientSecret) {
    clientSharedSecret = clientSecret;
  }

  public void setTokenSecret(String tokenSecret) {
    tokenSharedSecret = tokenSecret;
  }

  public String getSignatureMethod() {
    return "HMAC-SHA256";
  }

  public String computeSignature(String signatureBaseString) throws GeneralSecurityException {
    // compute key
    StringBuilder keyBuffer = new StringBuilder();
    String clientSecret = clientSharedSecret;
    if (clientSecret != null) {
      keyBuffer.append(OAuthParameters.escape(clientSecret));
    }
    keyBuffer.append('&');
    String tokenSecret = tokenSharedSecret;
    if (tokenSecret != null) {
      keyBuffer.append(OAuthParameters.escape(tokenSecret));
    }
    String key = keyBuffer.toString();
    // sign
    SecretKey secretKey = new SecretKeySpec(StringUtils.getBytesUtf8(key), "HmacSHA256");
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(secretKey);
    return Base64.encodeBase64String(mac.doFinal(StringUtils.getBytesUtf8(signatureBaseString)));
  }
}