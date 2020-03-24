
package jenaaplication;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;


public class TwitterWrapper {
    
    static Twitter setConfiguration(){
        Twitter twitter = TwitterFactory.getSingleton();
        return twitter;
    }
    
    public static List<Status> searchTweets(String term,int count) throws TwitterException{
        Twitter twitter = setConfiguration();
        List<Status> res = new ArrayList();
        Query query = new Query(term);
        try{          
            while(count > 0){
              query.count( count > 100 ? 100 : count);
              QueryResult result = twitter.search(query);
              res.addAll(result.getTweets());
              if (result.hasNext()){
                  query = result.nextQuery();
              }
              count -= result.getCount();
            }
        } catch(TwitterException te){
          System.out.println("Failed to search tweets: " + te.getMessage());
        }
        return res;
    }
    
    public static Status getTweet(long tweetId){
        Twitter twitter = setConfiguration();
        try{
            return twitter.showStatus(tweetId);
        }catch(TwitterException e){
            System.out.println("Tweet " + tweetId + " no encontrado");
        }
        
        return null;
    }
    
    public static boolean isTweet(long tweetId){
        Twitter twitter = setConfiguration();
        try{
            twitter.showStatus(tweetId);
        }catch(TwitterException e){
            System.out.println("Tweet " + tweetId + " no encontrado");
            return false;
        }
        
        return true;
    }
}
