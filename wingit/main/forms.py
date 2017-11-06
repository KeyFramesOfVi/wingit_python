from django import forms

class ContactForm(forms.Form):
	full_name = forms.CharField(required=False)
	email = forms.EmailField()
	message = forms.CharField(widget=forms.Textarea)

class filterForm(forms.Form):
	PRICES = [	('1','$'),
				('2','$$'),
				('3','$$$'),
				('4', '$$$$')]

	LOCATION = [	('Current', 'Current'),
				 	('Uptown', 'Uptown'),
					('Midtown', 'Midtown'),
					('Downtown', 'Downtown')
				]

	CATEGORY = [ ('bar','bar'),
				 ('cafe','cafe'),
				 ('restaurant','restaurant'),
				]

	price = forms.ChoiceField(widget=forms.RadioSelect,choices=PRICES,required=False)
	location = forms.ChoiceField(widget=forms.RadioSelect,choices=LOCATION,required=False)
	category = forms.ChoiceField(widget=forms.RadioSelect,choices=CATEGORY,required=False)

	reroll = forms.BooleanField(required=False)