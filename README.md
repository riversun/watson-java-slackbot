# Overview
Example of making slackbot with Watson

It is licensed under [MIT](https://opensource.org/licenses/MIT).

# About this example

When sending a direct message to slack bot, bot executes workspace of Watson Conversation as follows.

<img src="https://riversun.github.io/wcs/img/watson_slacklet_en.gif" width=75%>

# Source

```java

/**
 * 
 * Slack Bot with Watson Conversation
 * <p>
 * When sending a "direct message" to this SlackBot, this bot will execute the
 * dialog of Watson Conversation Workspace <br>
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
```

# maven

```xml
		<dependency>
			<groupId>org.riversun</groupId>
			<artifactId>wcs</artifactId>
			<version>1.0.2</version>
		</dependency>
		<dependency>
			<groupId>org.riversun</groupId>
			<artifactId>slacklet</artifactId>
			<version>1.0.2</version>
		</dependency>
```
