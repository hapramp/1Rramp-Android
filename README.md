# HapRamp Android App [![Build Status](https://travis-ci.org/hapramp/Hapramp-Android.svg?branch=master)](https://travis-ci.org/hapramp/Hapramp-Android)

HapRamp is a Steem Blockchain based social media for the creative communities. This is the Android application for users to use the platform.

### If you are a User.
---
**Pre-requisite**
You must have a [Steem](https://steemit.com/) Account.

**Screenshots:**

<img src="https://user-images.githubusercontent.com/10809719/42750953-cf4c0f38-8906-11e8-9bab-301baa46322d.png" width="320px" height="640px"/> --> <img src="https://user-images.githubusercontent.com/10809719/42751064-1fd1103e-8907-11e8-8638-76704c75e889.png" width="320px" height="640px"/>

You need to authorize hapramp by clicking `Continue` Button.

<img src="https://user-images.githubusercontent.com/10809719/42751169-84bdce92-8907-11e8-9890-55907ba427d4.png" width="320px" height="640px"/> --> <img src="https://user-images.githubusercontent.com/10809719/42751204-a241ce1e-8907-11e8-967a-2a3adf1b5788.png" width="320px" height="640px" alt="Home Page"/>

By Clicking `+` Button you can Create `Post` or `Blog`

<img src="https://user-images.githubusercontent.com/10809719/42751323-087261da-8908-11e8-93bb-4ad1edd24410.png" width="320px" height="640px" alt="Post Create"/> Or  <img src="https://user-images.githubusercontent.com/10809719/42751382-3732776c-8908-11e8-88df-9bfea4b5f415.png" width="320px" height="640px"/>

By clicking Youtube Symbol, you can embed Youtube video just by searching...
<p align="center"><img src="https://user-images.githubusercontent.com/10809719/42751492-92e5f26e-8908-11e8-86b7-059464305536.png" width="320px" height="640px"/></p>

---

**How to `Follow/Unfollow` Some one ?**

Clicking Search on Home Page take you to search page. Here you can type user name and get a list of suggestions. like:

<img src="https://user-images.githubusercontent.com/10809719/42751659-1c3c00d0-8909-11e8-986b-658f5214375c.png" width="320px" height="640px"/> --> <img src="https://user-images.githubusercontent.com/10809719/42751743-58b0d720-8909-11e8-9359-69dabcee2d80.png" width="320px" height="640px"/>

You can directly follow from the list of users, or can navigate to user`s full profile by tapping on it.
<p align="center"><img src="https://user-images.githubusercontent.com/10809719/42751915-f94a542c-8909-11e8-9d76-047e0c2299bc.png" width="320px" height="640px"/></p>

---

**User Wallet and Transaction History**

 By tapping on `GO TO WALLET` in user`s profile you can view wallet, below it you can check for the current value of *STEEM DOLLARS* and *STEEM* in the market.

<img src="https://user-images.githubusercontent.com/10809719/42752033-4ae5c7a8-890a-11e8-8428-a97ae800e8ca.png" width="320px" height="640px"/> --> <img src="https://user-images.githubusercontent.com/10809719/42752131-9aa8c696-890a-11e8-8334-0c088e091801.png" width="320px" height="640px"/>

Meanwhile, you can check transaction history just by tapping `GO TO HISTORY` button.

<p align="center"><img src="https://user-images.githubusercontent.com/10809719/42752275-f8c25d14-890a-11e8-91a9-195279bc878f.png" width="320px" height="640px"/></p>

---

### Feature List
 - **Login:** Users can Login using [SteemConnect](https://steemconnect.com).

 - **Communities:** Select the communities of your interest from the 9 pre-defined communities: Art, Dance, Design, Dramatics, Fashion, Literature, Music, Photography, and Travel.

 - **Feed:**  User feed will be generated based on their preference of the communities.

 - **Content Filter:** Users can switch between the specific community to receive filtered content.

 - **Content posting:** There are two ways to share content on HapRamp: Micro-blog multimedia post and Blog post.

 - **Multi-media:** The application supports Image, Audio file, and YouTube video embedding.

 - **Mirco-blogs:**  The Micro-blog multimedia posts can contain one media file with text and tags.

 - **Blog posts:** The Blog posts can contain multiple media files and has the support for formatting.

 - **Community Tag:** The users need to select the relevant communit(y/ies) (max 3) before posting the content.

 - **Comments:**  The application support comments on posts.

 - **Content Voting:** The Users can rate posts on a scale of 1 to 5 stars which signifies variable Steem voting power.

 - **Rewards:** The users earn Steem Dollars according to how much ratings they received from how many people.

 - **Steem Power:** The earning depends on the Steem Power of respective users that they accumulate over time by creating and curating content on the platform.

 - **User Profile:** On the user profile section, the users edit profile picture, bio, and interests.

 - **User Search:** Search existing Steem users from the search bar.

 - **Notification:** users will receive notifications for the ratings and comments on their own content.

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

