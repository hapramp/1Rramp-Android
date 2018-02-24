package com.hapramp.steem;

import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.api.client.json.Json;
import com.hapramp.preferences.HaprampPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import eu.bittrade.libs.steemj.SteemJ;
import eu.bittrade.libs.steemj.base.models.AccountName;
import eu.bittrade.libs.steemj.base.models.Asset;
import eu.bittrade.libs.steemj.base.models.BeneficiaryRouteType;
import eu.bittrade.libs.steemj.base.models.CommentOptionsExtension;
import eu.bittrade.libs.steemj.base.models.CommentPayoutBeneficiaries;
import eu.bittrade.libs.steemj.base.models.DynamicGlobalProperty;
import eu.bittrade.libs.steemj.base.models.Permlink;
import eu.bittrade.libs.steemj.base.models.SignedTransaction;
import eu.bittrade.libs.steemj.base.models.operations.CommentOperation;
import eu.bittrade.libs.steemj.base.models.operations.CommentOptionsOperation;
import eu.bittrade.libs.steemj.base.models.operations.Operation;
import eu.bittrade.libs.steemj.enums.AssetSymbolType;
import eu.bittrade.libs.steemj.exceptions.SteemCommunicationException;
import eu.bittrade.libs.steemj.exceptions.SteemInvalidTransactionException;
import eu.bittrade.libs.steemj.exceptions.SteemResponseException;

/**
 * Created by Ankit on 2/21/2018.
 */

public class SteemPostCreator {

    @WorkerThread
    public static void createPost(String body , String title , List<String> tags , PostStructureModel postStructure){

        SteemJ steemJ = SteemHelper.getSteemInstance();

        try {
            //author account of post
            String username = HaprampPreferenceManager.getInstance().getUsername();
            String __permlink = PermlinkGenerator.getPermlink();
            Log.d("TEST","Username "+username);
            Log.d("TEST","permalink "+__permlink);

            AccountName author = new AccountName(username);
            Permlink permlink = new Permlink(__permlink);

            boolean allowVotes = LocalConfig.ALLOW_VOTES;
            boolean allowCurationRewards = LocalConfig.ALLOW_CURATION_REWARDS;
            short percentSteemDollars = LocalConfig.PERCENT_STEEM_DOLLARS;
            String jsonMetadata = new JsonMetaDataModel(tags,postStructure).getJson();

            AccountName parentAuthor = null;  // new post
            Permlink parentPermlink = new Permlink(LocalConfig.PARENT_PERMALINK);
            CommentOperation commentOperation = new CommentOperation(parentAuthor, parentPermlink, author, permlink, title, body, jsonMetadata);
            ArrayList<Operation> operations = new ArrayList<>();
            operations.add(commentOperation);
            BeneficiaryRouteType beneficiaryRouteType = new BeneficiaryRouteType(new AccountName(LocalConfig.BENEFICIERY_ACCOUNT_NAME), LocalConfig.BENEFICIERY_COMMISSION);
            ArrayList<BeneficiaryRouteType> beneficiaryRouteTypes = new ArrayList<>();
            beneficiaryRouteTypes.add(beneficiaryRouteType);
            CommentPayoutBeneficiaries commentPayoutBeneficiaries = new CommentPayoutBeneficiaries();
            commentPayoutBeneficiaries.setBeneficiaries(beneficiaryRouteTypes);
            ArrayList<CommentOptionsExtension> commentOptionsExtensions = new ArrayList<>();
            commentOptionsExtensions.add(commentPayoutBeneficiaries);

            CommentOptionsOperation commentOptionsOperation = new CommentOptionsOperation(
                    author,
                    permlink,
                    new Asset(1000000000, AssetSymbolType.SBD),
                    percentSteemDollars,
                    allowVotes,
                    allowCurationRewards,
                    commentOptionsExtensions
            );

            operations.add(commentOptionsOperation);

            // final commit
            DynamicGlobalProperty globalProperties = steemJ.getDynamicGlobalProperties();
            SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations, null);
            signedTransaction.sign();
            steemJ.broadcastTransaction(signedTransaction);

        } catch (SteemCommunicationException e) {
            e.printStackTrace();
            Log.v("TEST", e.toString());
        } catch (SteemResponseException e) {
            e.printStackTrace();
            Log.v("TEST", e.toString());
        } catch (SteemInvalidTransactionException e){
            e.printStackTrace();
            Log.v("TEST", e.toString());
        }
    }

}
