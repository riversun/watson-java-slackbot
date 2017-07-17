/*
 * Copyright 2016-2017 Tom Misawa, riversun.org@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"), to deal in the 
 * Software without restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the 
 * Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 *  INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package org.example;

import java.io.IOException;

import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.riversun.wcs.WcsClient;
import org.riversun.xternal.simpleslackapi.SlackUser;

/**
 * 
 * Slack Bot with Watson Conversation
 * <p>
 * When sending a "direct message" to this SlackBot, this bot will execute the
 * dialog of Watson Conversation Workspace <br>
 * <br>
 * 
 * @see https://github.com/riversun/watson-conversation-service-for-java
 * @see https://github.com/riversun/slacklet
 * 
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class WcsSlackBotExample00 {

    // EDIT HERE FOR WATSON CONVERSAION CREDENTIALS
    private static final String WATSON_CONVERSATION_USERNAME = "EDIT_ME_USERNAME_HERE";
    private static final String WATSON_CONVERSATION_PASSWORD = "EDIT_ME_PASSWORD_HERE";
    private static final String WATSON_CONVERSATION_WORKSPACE_ID = "EDIT_ME_WORKSPACE_ID_HERE";

    // EDIT HERE FOR SLACK BOT API TOKEN
    private static final String SLACK_BOT_API_TOKEN = "EDIT_ME_SLACK_API_TOKEN";

    public static void main(String[] args) throws IOException {

        final WcsClient watson = new WcsClient(
                WATSON_CONVERSATION_USERNAME,
                WATSON_CONVERSATION_PASSWORD,
                WATSON_CONVERSATION_WORKSPACE_ID);

        SlackletService slackService = new SlackletService(SLACK_BOT_API_TOKEN);

        // Add slacklet for direct message
        slackService.addSlacklet(new Slacklet() {

            @Override
            public void onDirectMessagePosted(SlackletRequest req, SlackletResponse resp) {

                // When a direct message to BOT is posted

                // Slack user who sent the message
                SlackUser slackUser = req.getSender();

                // Message content (as text)
                String userInputText = req.getContent();

                // Get the id of the slack user and make it as Watson
                // Conversation's unique user id
                String wcsClientId = slackUser.getId();

                // Send the text inputed by the user to Watson and receive
                // Watson's response (outputText)
                String botOutputText = watson.sendMessageForText(wcsClientId, userInputText);

                // Display the response from Watson on slack
                slackService.sendDirectMessageTo(slackUser, botOutputText);

            }

        });

        // start service(connecting to slack)
        slackService.start();

    }

}
