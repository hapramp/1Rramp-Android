# 1Ramp Android App [![Build Status](https://travis-ci.org/hapramp/1Rramp-Android.svg?branch=master)](https://travis-ci.org/hapramp/1Rramp-Android) [![Discord](https://img.shields.io/discord/456730715598618624.svg)](https://discord.gg/r9vwcHe)
[<img src="https://user-images.githubusercontent.com/10809719/45163411-05b78680-b20e-11e8-9891-f0b8b6fcf23b.png" height="62px" width="161px"/>](https://play.google.com/store/apps/details?id=com.hapramp)

![header](https://user-images.githubusercontent.com/10809719/46408112-a7a79180-c72e-11e8-8232-0b972ea4195c.png)

1Ramp is a Steem Blockchain based social media for the creative communities. This is the Android application for users to use the platform.

![screenshot_1](https://user-images.githubusercontent.com/10809719/46408113-a7a79180-c72e-11e8-8d2f-ac6771e5211d.png)
![screenshot_2](https://user-images.githubusercontent.com/10809719/46408114-a8402800-c72e-11e8-8b51-1ac3730bdb2d.png)



### If you are a User.
---
**Prerequisite:**
You must have a [Steem](https://steemit.com/) Account to sign up on 1Ramp.
---

### Feature List
 - **Login:** Users can Login using [SteemConnect](https://steemconnect.com). Use your Steem username and password to login.

 - **Communities:** Select the communities of your interest from the pre-defined communities: Art, Dance, Design, Film, Fashion, Literature, Photography, and Travel.

 - **Feed:**  Your feed will show you the content shared by the users your follow on 1Ramp.

 - **Community filter:** You can switch between the communities of your choice to see content of your interest.

 - **Content sharing:** You have two ways to share content on 1Ramp: Post and Blog. Post means photos/text and blog means long form content with multiple images. The content posted on 1Ramp goes to the Steem blockchain and will be visible on other steem powered platforms (steemit, esteem etc).

 - **Community Tag:** You need to select the relevant communit(y/ies) (max 3) before posting the content. That helps your content to be visible to the people browsing in specific community.

 - **Comments:**  You can write comments on posts and reply to comments.

 - **Rating:** You can rate posts on a scale of 1 to 5 stars. It signifies feedback and results in an upvote with variable Steem voting power. 1-star means upvote with 20% power and 5-star means upvote with 100% power.

 - **Rewards:** You willl earn Steem Dollars based on the ratings you receive on your posts. The value of rewards depend on the Steem Power of those who rate you posts.

 - **Steem Power:** Your vote (rate) value depends on your Steem Power. Users with higher reputation (Steem Power) have higher vote value.

 - **User Profile:** You can find your posts in your profile. Accounts on 1Ramp (on Steem blockchain) in general are public by defalult. You can edit profile picture, bio, and interests from your profile.

 - **User Search:** Find any Steem user by their username from the search page.

---

### If You are a Developer

#### Android build dependency

 Minimum SDK Version: 19
 
 Target SDK Version: 27
 
 Build Tool Version: 27.0.3
 
 ### How to build the app
 
 **Debug Version**
 There are no special dependencies for building debug version of the app.
  - Clone the repo 
  - Perform a gradle sync
  - Run the app
 
 **Release Version**
 Building a release version of apk requires you a `keystore` file.
 For privacy reasons, we kept this file secret.
 
 You can generate a new `keystore` file using:
 `Build` -> `Generate Signed Apk`

### Security concerns for contributors
Any Private keys among 
**Active Key**,
**Owner Key**,
**Posting Key**

related to your account should not be pushed/commited along with codes. We are not liable for your accout`s security breach if done via code of this repository.

We do not store `Private Posting key` on users device.
Our servers do not store this key neither you should try to upload this key anywhere.

---

MIT License

Copyright (c) 2018 HapRamp

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

