SAFELIGHT - Is an app for people who are troubled about the safety of their information.
Lately I've been reading a lot about scam apps on the play store which have way too many permissions for a functionality such as that.

I felt that, even though some permissions are ok to be had (like wifi and network for ad revenue), there will always be those few developers who use those permissions to abuse their customers.
At the same time there are customers which see all developers as these strange entities or see them as huge evil companies that want their information and so the divide appears creating an us versus them situation, which is devastating for both sites.

I believe in transparency and so, I have built this basic application which does just that, functions as a flashlight for anyone to use. I put it on google play but I also want to share the full source here. The only thing missing are the sound effects and the images as I have no right to distribute them like this. 
Anyone can hook this into Eclipse or Android Studio and have their own working version. 

You can see the permissions I use in the manifest are :

    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />

I've seen writen in articles how this is terrifying, how it abuses your right, or how the app could take pictures of you, but think
about this, even if the app took pictures, how would they go anywhere? There's no internet connection permission. 
In the links section of this README I've placed a link which will show you why the permission is needed and how it affects your phone.

If anyone reads this and is interested in sparking up a conversation about developers, our work or just to understand what we actually do,
please whire to me on facebook, link bellow as well.



--------------------------------------------------------------------------------------------------------------------------------
LINKS 

Google Camera Documentation : http://developer.android.com/reference/android/hardware/Camera.html
My Facebook : https://www.facebook.com/ted.blop
Google play page : // TO BE ADDED
