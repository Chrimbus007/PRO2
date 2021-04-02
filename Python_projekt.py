import tweepy
import re
from textblob import TextBlob #Modul za analizo besedila
import matplotlib


 
class TwitterUporabnik():
    """Razred za ustvarjanje Twitter racuna z danimi kljuci"""
    def __init__(self): #avtentikacija racuna in dostop do twitter API-ja
        #kljuci za dostop do API
        upKljuc = "rNPAEDNseJN2vUjAHnUSvNXYs"
        upSecret = "qkD3sESbXAJilKdMo9ULMSs6HbOabPM3go202U82PK4r1yMuoo"
        dostopKljuc = "882672544750477313-keufKKwkflZoe7C50YGYFt8xDBvp0kb"
        dostopSecret = "QujS6EOGBBoNnhmzbHJLO05mBrAFkJ1CP34JWcbBN6N79"
        try:
            self.auth = tweepy.OAuthHandler(upKljuc, upSecret)
            self.auth.set_access_token(dostopKljuc, dostopSecret)
            self.api = tweepy.API(self.auth)
        except:
            print("Napaka pri avtentikaciji uporabnika")
        
    def ciscenje(self, twit): #scisti morebitne posebne znake v izpisu twita kot so @, #, $ etc.
        return "".join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)",' ', x).split())
        
    
    def mnenjeTwita(self, twit):
        """Pridobi mnenje glede na besedilo twita"""
        besediloTwita = TextBlob(self.ciscenje(twit)) 
        if besediloTwita.sentiment.polarity < 0:
            return "Negativno"
        elif besediloTwita.sentiment.polarity == 0:
            return "Nevtralno"
        else:
            return "Pozitivno"
        
        
                              
    def zajemTwitov(self, trend, st = 20):
        """Funkcija za zajem twitov za dolocen trend iz API-ja"""
        try:
            zajetiTwiti = []
            iskanjeTwitov = self.api.search(q = trend, count = st) 
            for twit in iskanjeTwitov:
                vzetiTwit = {}
                vzetiTwit["text"] = twit.text
                vzetiTwit["mnenje"] = self.mnenjeTwita(twit.text)   
            if twit.retweet_count > 0:
                if vzetiTwit not in zajetiTwiti:
                    zajetiTwiti.append(vzetiTwit)
            else:
                zajetiTwiti.append(vzetiTwit)
            return zajetiTwiti
        except tweepy.TweepError as x:
            print("Napaka: " + str(x))
    


def main():   
    api = TwitterUporabnik()
    twiti = api.zajemTwitov(trend = "Colorado", st = 200)
    pozitivni = [twit for twit in twiti if twit["mnenje"] == "Pozitivno"]
    negativni = [twit for twit in twiti if twit["mnenje"] == "Negativno"]
    nevtralni = [twit for twit in twiti if twit["mnenje"] == "Nevtralno"]
    print("Pozitivnih twitov za ta trend je {} %".format(len(pozitivni)*100/len(twiti)))
    print("Negativnih twitov za ta trend je {} %".format(len(negativni)*100/len(twiti)))
    print("Nevtralnih twitov za ta trend je {} %".format(len(nevtralni)*100/len(twiti)))
    
    for twit in pozitivni[:10]:
        print(twit["text"])
        
        
if __name__ == "__main__":
    main()

    
    

            
            
        
        
    
    
    
    
    

    
    
    


    
        

