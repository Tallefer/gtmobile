/**
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crackj2ee.midp.gtalk;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 * Login form.
 * 
 * @author xuefeng
 */
public class LoginForm extends Form implements CommandListener {

    // a test gmail account, please DO NOT change the password:
    private TextField username = new TextField("Gmail", "midp.talk", 30, TextField.ANY);
    private TextField password = new TextField("Password", "midp.java", 30, TextField.PASSWORD);

    private Command login = new Command("Sign in", Command.OK, 0);

    public LoginForm() {
        super("Sign in");
        append(username);
        append(password);
        addCommand(login);
        setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if(c==login) {
            String u = username.getString();
            String p = password.getString();
            int at = u.indexOf('@');
            if(at>0) {
                u = u.substring(0, at);
            }
            GTalkMIDlet.switchUI(new MainCanvas(u, p));
        }
    }

}
