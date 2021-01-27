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

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;

import de.mhus.osgi.api.MOsgi;
import de.mhus.osgi.api.karaf.AbstractCmd;
import de.mhus.osgi.crypt.api.signer.SignerProvider;

@Command(scope = "crypt", name = "signer-list", description = "Signer list")
@Service
public class CmdSignerList extends AbstractCmd {

    @Override
    public Object execute2() throws Exception {

        for (MOsgi.Service<SignerProvider> ref :
                MOsgi.getServiceRefs(SignerProvider.class, null)) {
            System.out.println(ref.getReference().getProperty("signer"));
        }
        return null;

    }

}
