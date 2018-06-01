#### Repository
https://github.com/hapramp/Hapramp-Android

### Added STEEM Power and STEEM Balance section
- User can now see his/her `SBD` balance
- User can see his/her `STEEM` power.

Screenshot:
![](https://github.com/hapramp/Hapramp-Android/blob/master/screenshots/steem_balance.png)

### Implementation

STEEM power is not directly provided by steem api.
It needs to be calculated at client end.

Formulae for calculating STEEM Power is:
```

steem_power = (total_vesting_fund_steem * user_vesting_share ) / total_vesting_share

```

In order to get STEEM Power, we needed 
 - `total_vesting_fund_steem`
 - `total_vesting_share`
 - `user_vesting_share`

First two of these( `total_vesting_fund_steem` , `total_vesting_share`) can be retrieve from `SteemJ` library method call.

This library is equipped with a helper method named `getDynamicGlobalProperties()`.

Sample call:
```
 // SteemJ init
 SteemJ steemJ = SteemHelper.getSteemInstance(); // SteemHelper is a class which returns configured SteemJ Instance
 
 try {
     DynamicGlobalProperty dynamicGlobalProperty = steemJ.getDynamicGlobalProperties();
      float totalVestingShares = dynamicGlobalProperty.getTotalVestingShares().getAmount();
      float totalVestingFundSteem = dynamicGlobalProperty.getTotalVestingFundSteem().getAmount();
 } catch (SteemCommunicationException e) {
 
 } catch (SteemResponseException e) {
 
 }
```

Now the remaining is `user_vesting_share`.
This variable is part of `Account` model class of `SteemJ` library.
We need `Account` of user for which balance is fetched.

In SteemJ library provides us a method to access user accounts.

The method has the following signature.
```
public List<ExtendedAccount> getAccounts(List<AccountName> accountNames)
```
Its documentation says
>  * `accountNames` A list of accounts you want to request the details for.
>  * returns A List of accounts found for the given account names.

The following method returns the user account given the STEEM `username`

```
public ExtendedAccount getUserAccount(String username) throws SteemCommunicationException, SteemResponseException {
        JsonRPCRequest requestObject = new JsonRPCRequest();
        requestObject.setSteemApi(SteemApiType.DATABASE_API);
        requestObject.setApiMethod(RequestMethods.GET_ACCOUNTS);
        // The API expects an array of arrays here.
        String[] innerParameters = new String[1];
        innerParameters[0] = username;
        String[][] parameters = {innerParameters};
        requestObject.setAdditionalParameters(parameters);
        List<ExtendedAccount> accounts = communicationHandler.performRequest(requestObject, ExtendedAccount.class);
        if(accounts.size()>0){
            return accounts.get(0);
        }
        return null;
    }

```

and finally we can get `user_vesting_share` as:
```
ExtendedAccount extendedAccount = steemJ.getUserAccount("bxute"); //hard-coded user name. `steemJ` is an `SteemJ` instance
float userVestingShare = extendedAccount.getVestingShares().getAmount();

```

At the end we can apply the formulae for calculating STEEM Power to get it.

#### GitHub Account
https://github.com/bxute
