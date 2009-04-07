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
package com.javaeedev.midp.gtalk.component;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.javaeedev.midp.gtalk.MainCanvas;
import com.javaeedev.midp.gtalk.jabber.Friend;

/**
 * List & manage all friends.
 * 
 * @author xuefeng
 */
public class FriendList extends ComponentUI {

    private Image[] status;
    private int lineHeight;
    private int visibleLines;
    private int textWidth;

    private Vector friends = new Vector();
    private int selection = (-1);

    private Scrollable scrollable;

    public FriendList(int x, int y, int width, int height, Font font, Image[] status, Scrollable scrollable) {
        super(x, y, width, height, font);
        this.scrollable = scrollable;
        this.status = status;
        this.textWidth = width - 14;
        System.out.println("TextWidth: " + textWidth);
        lineHeight = font.getHeight() + 2;
        visibleLines = (height-2) / lineHeight;
    }

    public void paint(Graphics g) {
        super.paint(g);
        // update scrollable:
        scrollable.setScroll(friends.size(), selection);
        scrollable.paint(g);
    }

    protected void paintInternal(Graphics g) {
        // draw:
        g.setColor(MainCanvas.COLOR_BACKGROUND);
        g.fillRect(0, 0, width, height);
        //g.setColor(MainCanvas.COLOR_BORDER);
        //g.drawRect(0, 0, width-1, height-1);
        if(friends.size()==0)
            return;
        // draw each friend:
        synchronized(this) {
            int start = 0;
            int end = friends.size();
            if(end>visibleLines) {
                start = selection - visibleLines / 2;
                if(start + visibleLines > friends.size()) {
                    // selection is near end:
                    start = friends.size() - visibleLines;
                }
                if(start<0)
                    start = 0;
                end = start + visibleLines;
            }
            for(int i=start; i<end; i++) {
                Friend f = (Friend)friends.elementAt(i);
                // draw state icon:
                g.drawImage(status[f.getState()], 2, lineHeight*(i-start)+6, Graphics.LEFT|Graphics.TOP);
                if(i==selection) {
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_BACKGROUND);
                    g.fillRect(12, lineHeight*(i-start)+2, textWidth, lineHeight);
                    g.setColor(MainCanvas.COLOR_HIGHLIGHTED_FOREGROUND);
                }
                else {
                    g.setColor(MainCanvas.COLOR_FOREGROUND);
                }
                g.drawString(f.getDisplayName(), 12, lineHeight*(i-start)+2, Graphics.LEFT|Graphics.TOP);
            }
        }
    }

    public synchronized Friend getSelectedFriend() {
        if(selection>=0 && selection<friends.size())
            return (Friend)friends.elementAt(selection);
        return null;
    }

    public synchronized Friend findFriend(String email) {
        for(int i=0; i<friends.size(); i++) {
            Friend f = (Friend)friends.elementAt(i);
            if(f.getEmail().equals(email))
                return f;
        }
        return null;
    }

    public synchronized boolean addFriend(String email, String name) {
        if(name==null || "".equals(name.trim())) {
            int n = email.indexOf('@');
            if(n==(-1))
                name = email;
            else
                name = email.substring(0, n);
        }
        for(int i=0; i<friends.size(); i++) {
            Friend f = (Friend)friends.elementAt(i);
            if(f.getEmail().equals(email)) {
                if(f.getName().equals(name))
                    return false;
                friends.removeElementAt(i);
                break;
            }
        }
        friends.addElement(new Friend(email, name, suitable(name, textWidth)));
        // adjust selection:
        if(selection==(-1))
            selection = 0;
        return true;
    }

    public synchronized boolean removeFriend(String email) {
        for(int i=0; i<friends.size(); i++) {
            if(((Friend)friends.elementAt(i)).getEmail().equals(email)) {
                friends.removeElementAt(i);
                // adjust selection:
                if(selection>=(friends.size()-1))
                    selection--;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean setFriendStatus(String email, int state) {
        for(int i=0; i<friends.size(); i++) {
            Friend f = (Friend)friends.elementAt(i);
            if(f.getEmail().equals(email)) {
                if(f.getState()!=state) {
                    f.setState(state);
                    System.out.println("Set " + email + "'s state as " + state);
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized boolean moveUp() {
        if(selection>0) {
            selection--;
            return true;
        }
        return false;
    }

    public synchronized boolean moveDown() {
        if(selection<friends.size()-1) {
            selection++;
            return true;
        }
        return false;
    }
}
