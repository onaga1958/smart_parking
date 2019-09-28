from django.contrib import admin
from django.urls import path
from .views import FindParkingsEndpoint

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/find_parkings/', FindParkingsEndpoint.as_view())
]
