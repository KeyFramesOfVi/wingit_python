from django.conf.urls import url

from . import views

urlpatterns = [
	url(r'^$', views.home, name='home'),
	url(r'^about', views.about, name='about'),
	url(r'^wingit', views.wingit, name='wingit'),
	url(r'^contact', views.contact, name='contact'),
	url(r'^analytics', views.analytics, name='analytics'),
	url(r'^profile', views.profile, name='profile'),
]