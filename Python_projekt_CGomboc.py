import tweepy
import re
import textblob #modul za analizo besedil
from matplotlib import pyplot as plt #modul za risanje grafov

class TwitterUporabnik():
   """Ustvari uporabnika z danimi kljuci"""
   def __init__(self):
       upKljuc = 'rNPAEDNseJN2vUjAHnUSvNXYs'
       upSecret = 'qkD3sESbXAJilKdMo9ULMSs6HbOabPM3go202U82PK4r1yMuoo'
       dostopKljuc = '882672544750477313-keufKKwkflZoe7C50YGYFt8xDBvp0kb'
       dostopSecret = 'QujS6EOGBBoNnhmzbHJLO05mBrAFkJ1CP34JWcbBN6N79'
       try:
           self.auth = tweepy.OAuthHandler(upKljuc, upSecret)		
           self.auth.set_access_token(dostopKljuc, dostopSecret)		
           self.api = tweepy.API(self.auth)
       except:
           print("Napaka pri avtentikaciji")

   def ciscenje(self, twit):
       """Pocisti besedilo twita s tem da se znebi morebitnih posebnih znakov in linkov iz besedila"""
       return " ".join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)", " ", twit).split()) 

   def mnenje(self, twit):
       """izpise mnenje glede na besedilo twita"""
       cist_twit = textblob.TextBlob(self.ciscenje(twit)) #uporaba predhodnje funkcije za ciscenje besedila twita
       if cist_twit.sentiment.polarity > 0: #textblob ocenjuje mnenje kot interval od (-1.0, 1.0)
           return "pozitivno"
       elif cist_twit.sentiment.polarity == 0:
           return "nevtralno"
       else:
           return "negativno"

   def zajemTwitov(self, query, count = 20):
       """Zajame twite za analizo"""
       twiti = []
       try:
           iskaniTwiti = self.api.search(q = query, count = count) #query kot iskalni niz, count kot stevilo twitov
           for twit in iskaniTwiti:
               vzetiTwiti = {}
               vzetiTwiti["text"] = twit.text
               vzetiTwiti["mnenje"] = self.mnenje(twit.text)
               if twit.retweet_count > 0:
                   if vzetiTwiti not in twiti:
                       twiti.append(vzetiTwiti)
               else:
                   twiti.append(vzetiTwiti)
           return twiti
       except tweepy.TweepError as x: 
           print("Napaka : " + str(x))

def main(): #glavna funkcija
	api = TwitterUporabnik()
	n = input("Vnesite kljucno besedo: ")
	twiti = api.zajemTwitov(query = n, count = 100)
	
        #V posamezen list spravimo pozitivne in negativne twite za temo in izracunamo % mnenja
	pozTwiti = [twit for twit in twiti if twit["mnenje"] == "pozitivno"]
	negTwiti = [twit for twit in twiti if twit["mnenje"] == "negativno"]
	print("Pozitivnih twitov je: {} %".format(100*len(pozTwiti)/len(twiti)))
	print("Negativnih twitov je: {} %".format(100*len(negTwiti)/len(twiti)))
	print("Nevtralnih twitov je: {} %".format(100*(len(twiti) - (len(negTwiti) + len(pozTwiti)))/len(twiti)))
	
	#Izpis v obliki grafa
	poz = int(100*len(pozTwiti)/len(twiti))
	neg = int(100*len(negTwiti)/len(twiti))
	nev = int(100*(len(twiti) - (len(negTwiti) + len(pozTwiti)))/len(twiti))
	oznake = ["Pozitivni", "Negativni", "Nevtralni"]
	podatki = [poz, neg, nev]
	plt.pie(podatki, labels = oznake, colors = ["skyblue", "salmon", "lightgreen"], autopct = "%1.1f%%", wedgeprops = {"linewidth": 7, "edgecolor" : "white"})
	#dodatek za graf v obliki krofa
	krog = plt.Circle((0, 0), 0.4, color = "white") 
	p = plt.gcf()
	p.gca().add_artist(krog)
	plt.show()
	
	
	#izpise prvih par twitov v seznamu twiti, ne deluje vedno zaradi tezav z kodiranjem s strani twitterja
	print("\n \nTwiti:")
	for twit in twiti[:20]:
            print(twit["text"])
            
	#Izpise prvih par negativnih twitov, enaka tezava z kodiranjem
	print("\n \nNegativnih twitov:")
	for twit in negTwiti[:10]:
            print(twit["text"])
            
	#izpise prvih par pozitivnih twitov, enaka tezava z kodiranjem
	print("\n \nPozitivnih twitov:")
	for twit in pozTwiti[:10]:
            print(twit["text"])
        
        



        
            
        
            
        
        
       

	
	
    

if __name__ == "__main__":
    main()
##