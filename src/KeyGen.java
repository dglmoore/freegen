/*
 * Copyright 2012 Douglas Moore, Baylor University, Department of Physics
 *
 * This file is part of FreeGen.
 *
 * FreeGen is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * FreeGen is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * FreeGen.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.security.SecureRandom;
import java.math.BigInteger;

public final class KeyGen {
  private SecureRandom random = new SecureRandom();

  public String nextID() {
    return new BigInteger(130, random).toString(32);
  }
}
