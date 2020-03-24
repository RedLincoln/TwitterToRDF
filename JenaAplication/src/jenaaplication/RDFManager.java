package jenaaplication;

import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import twitter4j.Status;


public class RDFManager {
    private Model model;
    
    private String tweetURI = "http://tweets.com/tweetResource/";
    private String userURI = "http://tweets.com/userResource/";
    private String languageURI = "http://tweets.com/languageResource/";
    private String replyURI = "http://tweets.com/replyResource/";
    private String themeURI = "http://tweets.com/themeResource/";
    private String searchURI = "http://tweets.com/searchResource/";
    
    private String textTheme;
    private String searchString;
    
    //tweet
    Property propertyText;
    Property propertyTweetId;
    Property propertyDate;
    Property propertyReply;
    Property propertyUser;
    Property propertyLanguage;
    Property propertySearch;
    
    //UserTema a buscar
    Property propertyUserName;
    Property propertyUserLocation;
    
    //Language
    Property propertyLanguageCode;
    Property propertyLanguageLabel;
    
    //ThemeuserURI
    Property propertyTheme;
    Property propertyThemeLabel;
    
    public RDFManager(String textTheme, String searchString){
        model = ModelFactory.createDefaultModel();
        this.textTheme = textTheme;
        this.searchString = searchString;
        
        propertyText = model.createProperty(tweetURI, "texto");
        propertyDate = model.createProperty(tweetURI, "fecha_creación");
        propertyTweetId = model.createProperty(tweetURI, "id_tweet");
        propertyReply = model.createProperty(tweetURI, "respuesta_a");
        propertyUser = model.createProperty(tweetURI, "autor");
        propertyLanguage = model.createProperty(tweetURI, "idioma");
        propertySearch = model.createProperty(tweetURI, "relacionado_con");
        
        propertyUserName = model.createProperty(userURI, "nombre");
        propertyUserLocation = model.createProperty(userURI, "ubiciación");
        
        propertyLanguageCode = model.createProperty(languageURI, "id_idioma");
        propertyLanguageLabel = model.createProperty(languageURI, "etiqueta");
        
        propertyTheme = model.createProperty(searchURI, "tipo");
        propertyThemeLabel = model.createProperty(themeURI, "etiqueta");
                
    }
    
    public Resource createGraph(Status status){
        return createGraph(status, true);
    }
    
    public Resource createGraph(Status status, boolean checkReply){
        GraphData data = new GraphData(status);
        
        Resource tweet = createTweetResource(data);
        Resource user = createUserResource(data);
        Resource language = createLanguageResource(data);
        Resource search = createSearchResource();
        Resource theme = createThemeResource();
        
        search.addProperty(RDF.type, theme);
        tweet.addProperty(propertySearch, search);
        tweet.addProperty(propertyUser, user);
        tweet.addProperty(propertyLanguage, language);
        
        
        
        if (checkReply && data.isReply() && TwitterWrapper.isTweet(data.getReplyTo())){
            tweet.addProperty(propertyReply, createGraph(TwitterWrapper.getTweet(data.getReplyTo()), false));
        }
        
        return tweet;
    }

    private Resource createTweetResource(GraphData data) {
        Resource tweet = model.createResource(tweetURI + data.getTweetId());
        tweet.addProperty(propertyTweetId, data.getTweetId() + "");
        tweet.addProperty(propertyText, data.getText());
        tweet.addProperty(propertyDate, data.getDate());
        
        return tweet;
    }

    private Resource createUserResource(GraphData data) {
        Resource user = model.createResource(userURI + data.getUserId());
        user.addProperty(propertyUserName, data.getUserName());
        user.addProperty(propertyUserLocation, data.getUserLocation());
        
        return user;
    }

    private Resource createLanguageResource(GraphData data) {
        Resource language = model.createResource(userURI + data.getLanguageId());
        language.addProperty(propertyLanguageCode, data.getLanguageId());
        language.addProperty(propertyLanguageLabel, data.getLanguageLabel());
        
        return language;
    }    

    private Resource createThemeResource() {
        Resource theme = model.createResource(themeURI + textTheme);
        theme.addProperty(RDF.type, RDFS.Class);
        theme.addProperty(RDFS.label, textTheme);
        
        return theme;
    }
    
    private Resource createSearchResource(){
        Resource search = model.createResource(searchURI + searchString);
        
        return search;
    }

    public Model getModel() {
        return model;
    }

}
