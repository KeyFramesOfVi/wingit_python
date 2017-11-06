from __future__ import unicode_literals

from django.contrib.auth.models import User

from mongoengine import *

# This is used to fully describe the feature vector passed to the neural network
class restaurantUser(Document):
	username = StringField(max_length=30)
	American = FloatField(min_value=0, max_value=1)
	French   = FloatField(min_value=0, max_value=1)
	Italian  = FloatField(min_value=0, max_value=1)
	Spanish  = FloatField(min_value=0, max_value=1)
	Indian   = FloatField(min_value=0, max_value=1)
	Asian    = FloatField(min_value=0, max_value=1)


# class userPrefs(models.Model):
# 	user = models.OneToOneField(User, on_delete=models.CASCADE)

# 	PRICES = [ '1','2','3','4']
# 	LOCATION = [ 'Current', 'Uptown', 'Midtown', 'Downtown']
# 	CATEGORY = [ 'bar', 'cafe','restaurant']

# 	pricepref = models.CharField(max_length=1,choices=PRICES,default='2')
# 	locationpref = models.CharField(max_length=8,choices=LOCATIOn, default='Current')