from django.conf import settings
from django.core.mail import send_mail
from django.shortcuts import render
from .forms import ContactForm, filterForm

# Mongodb models
from .models import restaurantUser

# wingitService packages
import requests, json

# random 
import random

# Mongoengine
import mongoengine

import os

def home(request):
	context = {
	}

	return render(request, 'home.html', context)

def about(request):
	context = {

	}
	return render(request, 'about.html', context)

def contact(request):
	form = ContactForm(request.POST or None)
	if form.is_valid():
		form_email = form.cleaned_data.get('email')
		form_message = form.cleaned_data.get('message')
		form_full_name = form.cleaned_data.get('full_name')

		subject = 'Contact Form: Message from %s' %(form_email)
		from_email = settings.EMAIL_HOST_USER
		to_email = [from_email]

		contact_message = '%s: %s via %s' %(form_full_name, form_message, form_email)

		send_mail(subject, 
				  contact_message, 
				  from_email, 
				  to_email, 
				  fail_silently=False)

	context = {
		'form': form,
	}
	return render(request, 'contact.html', context)

def profile(request):
	context = {

	}
	return render(request, 'profile.html', context)

def wingit(request):

	# Queries azure based on the user preferences passed as a parameter
	# These preferences are combined with the current day and time 
	# and a query is prepared.
	# The suggestion is returned as a JSON object. 
	# If azure is unavailable or the query returns an error, a random type of food is chosen


	def getSuggestion(reroll):
		# Temporary random selection of the available food types
		# Placeholder for future azure neural network call
		foodTypes = ['italian','american','mexican','french','spanish','indian','asian']
		typeOfFood = random.choice(foodTypes)


		# Get user doc with matching username from mongodb
		docExists = restaurantUser.objects(username=str(request.user)).count()

		if docExists >= 1:
			prefs = restaurantUser.objects(username=str(request.user))[0]

			# If the user rerolls his result, update their feature vector
			# if reroll == True:
			# 	prefs.update()
				# Update Example:
				# prefs.update(American=.1,French=.3,Italian=.7)

		else:
			# if doesn't exist, add doc (with default weights: .5 across):
			prefs = restaurantUser.objects.create(
					username = str(request.user),
					American = .5,
					French   = .5,
					Italian  = .5,
					Spanish  = .5,
					Indian   = .5,
					Asian    = .5
			)
			prefs.save()

		return typeOfFood

	form = filterForm(request.POST or None)

	# Default values
	listOfPlaces = []
	typeOfFood = ''
	displayResults = False
	displayPhoto = False
	photo = 'sample.jpg'

	#Default form values:
	price = '2'
	form_loc = 'Current'
	form_cat = 'restaurant'


	if request.method == 'POST' and form.is_valid():
		price = form.cleaned_data.get('price') or '2'
		form_loc = form.cleaned_data.get('location') or 'Current'
		form_cat = form.cleaned_data.get('category') or 'restaurant'
		reroll = form.cleaned_data.get('reroll')

		if form_cat == 'restaurant':
			typeOfFood = getSuggestion(reroll)
		
		# Lookup location selected to replace with coordinates
		LOCATION = {	'Current':'40.767281,-73.965779',
				 		'Uptown':'40.8240,-73.9448',
						'Midtown':'40.7549,-73.9840',
						'Downtown':'40.7230,-74.0006'
					}
		latlng = LOCATION[form_loc]

		# Debug info
		# print form_cat
		# print price
		# print form_loc
		# print typeOfFood


		APIKEY = settings.APIKEY

		query = 'https://maps.googleapis.com/maps/api/place/nearbysearch/json' + '?' + 'key=' + APIKEY + '&rankby=distance' + '&type='+ form_cat + '&minprice=' + price +'&maxprice=' + price + '&location=' + latlng + '&keyword=' + typeOfFood + '&opennow'
		r = requests.get(query)

		# Successful Query, can be parsed for result
		if str(r) == '<Response [200]>':
			rJson = json.loads(r.content)

			# Results dumped for testing
			# now write output to a file
			#jsontxt = open("results.json", "w")
			#jsontxt.write(json.dumps(json.loads(r.content), indent=4))
			#jsontxt.close()


			# for place in rJson['results']:
			# 	# storing all necessary information of each place in list of results 
			# 	latlong = str(place['geometry']['location']['lat']) + ',' + str(place['geometry']['location']['lng'])
			#  	placeTuple = [ place['name'], place['vicinity'], latlong ]
			#  	listOfPlaces.append(placeTuple)


			# Random Result test:
			if len(rJson['results']) > 0:
				place = random.choice(rJson['results'])
				latlong = str(place['geometry']['location']['lat']) + ',' + str(place['geometry']['location']['lng'])
				placeTuple = [ place['name'], place['vicinity'], latlong ]
				listOfPlaces.append(placeTuple)

		#else:
			# Query Failed

		if len(listOfPlaces) > 0:
			displayResults = True

			# Get photo for first result
			if u'photos' in place:
				imgref = place[u'photos'][0][u'photo_reference']

				# The result of the following query is an image?
				query = requests.get('https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=' + imgref + '&key=' + APIKEY)

				photo = 'https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=' + imgref + '&key=' + APIKEY
				displayPhoto = True

				#print 'https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=' + imgref + '&key=' + APIKEY
				#phototxt = open("photo.txt","w")
				#phototxt.write(query.content)
				#phototxt.close()


		#print 'not valid'

	context = {
		'form':form,
		'displayResults':displayResults,
		'type':typeOfFood,
		'places':listOfPlaces,
		'photourl':photo,
		'displayPhoto':displayPhoto,

		# Values to set 
		'price':price,
		'loc':form_loc,
		'cat':form_cat,

	}

	return render(request, 'wingit.html', context)

# https://maps.googleapis.com/maps/api/place/photo?photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyBXV-igqt3Q8PAle46RPEDdD4Pd7KKJ2hU&
# https://www.google.com/maps?q=lat,long


# Analytics
def analytics(request):
	context = {
	}

	return render(request, 'analytics.html', context)