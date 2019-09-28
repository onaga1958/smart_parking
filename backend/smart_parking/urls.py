from django.contrib import admin
from django.urls import path
from .views import FindParkingsTimeEndpoint, FindParkingsOriginEndpoint

urlpatterns = [
    path('admin/', admin.site.urls),
    path(
        'api/find_parkings/<str:destination>/time/<str:arrival_time>',
        FindParkingsTimeEndpoint.as_view(),
    ),
    path(
        'api/find_parkings/<str:destination>/origin/<str:origin>',
        FindParkingsOriginEndpoint.as_view(),
    ),
]
