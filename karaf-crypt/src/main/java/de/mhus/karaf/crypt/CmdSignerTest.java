/**
 * Copyright (C) 2019 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mhus.karaf.crypt;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.lib.core.IProperties;
import de.mhus.lib.core.M;
import de.mhus.lib.core.MProperties;
import de.mhus.lib.core.crypt.pem.PemBlock;
import de.mhus.lib.core.crypt.pem.PemKey;
import de.mhus.lib.core.crypt.pem.PemPair;
import de.mhus.lib.core.util.Lorem;
import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.crypt.api.CryptApi;
import de.mhus.osgi.crypt.api.signer.SignerProvider;

@Command(scope = "crypt", name = "signer-test", description = "Signer Handling")
@Service
public class CmdSignerTest extends AbstractCmd {

    @Argument(
            index = 0,
            name = "signer",
            required = true,
            description = "Selected signer",
            multiValued = false)
    String signer;

    @Argument(
            index = 1,
            name = "paramteters",
            required = false,
            description = "Parameters",
            multiValued = true)
    String[] parameters;

    @Option(
            name = "-p",
            aliases = {"--passphrase"},
            description = "Define a passphrase if required",
            required = false,
            multiValued = false)
    String passphrase = null;

    @Override
    public Object execute2() throws Exception {

        SignerProvider prov = M.l(CryptApi.class).getSigner(signer);

        MProperties p = IProperties.explodeToMProperties(parameters);
        if (passphrase != null) p.setString(CryptApi.PASSPHRASE, passphrase);
        String text = Lorem.create(p.getInt("lorem", 1));

        System.out.println(text);

        PemPair keys = prov.createKeys(p);
        System.out.println(keys.getPublic());
        System.out.println(new PemKey((PemKey) keys.getPrivate(), false));

        PemBlock sign = prov.sign(keys.getPrivate(), text, passphrase);
        System.out.println(sign);

        boolean valide = prov.validate(keys.getPublic(), text, sign);
        System.out.println("Valide: " + valide);

        return null;
    }
}
