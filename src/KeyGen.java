/*
 * Copyright 2014 Douglas Moore. All rights reserved.
 * Use of this source code is governed by the MIT
 * license that can be found in the LICENSE file.
 */

import java.security.SecureRandom;
import java.math.BigInteger;

public final class KeyGen {
    private SecureRandom random = new SecureRandom();

    public String nextID() {
        return new BigInteger(130, random).toString(32);
    }
}
