
package jenaaplication;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import twitter4j.Status;
import twitter4j.User;

public class GraphData {
    private String text;
    private long tweetId;
    private String date;
    private long replyTo;
    private long userId;
    private String userName;
    private String userLocation;
    private String languageId;
    private String languageLabel;

    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserLocation() {
        return userLocation;
    }

    
    public GraphData(Status tweet){
        // User
        User user = tweet.getUser();
        userLocation = user.getLocation();
        userName = user.getName();
        userId = user.getId();
               
        //tweet
        text = tweet.getText();
        date = formatDate(tweet.getCreatedAt());
        tweetId = tweet.getId();
        
        // retweet
        replyTo = tweet.getInReplyToStatusId();
        
        // Lenguage
        languageId = tweet.getLang();
        languageLabel = new Locale(languageId).getDisplayLanguage();
    } 
    
       
    private String formatDate(Date date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate()
                   .format(formatter);
        
    }
    
    public String getText() {
        return text;
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getDate() {
        return date;
    }

    public long getReplyTo() {
        return replyTo;
    }

    public String getLanguageId() {
        return languageId;
    }

    public String getLanguageLabel() {
        return languageLabel;
    }
    
    public boolean isReply(){
        return replyTo != -1;
    }
    
}
