package com.hapramp.steem;

import android.os.Handler;
import android.support.annotation.WorkerThread;
import android.util.Log;
import android.util.TimeUtils;

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
    private Handler mHandler;
    public SteemPostCreator() {
        this.mHandler = new Handler();
    }
    @WorkerThread
    public void createPost(final String body, final String title, final List<String> tags, final PostStructureModel postStructure, final String __permlink) {
        new Thread() {
            @Override
            public void run() {
                SteemJ steemJ = SteemHelper.getSteemInstance();
                if (steemJ == null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            steemPostCreatorCallback.onPostCreationFailedOnSteem("Failed To Initialize Steem");
                        }
                    });
                    return;
                }
                try {
                    String username = HaprampPreferenceManager.getInstance().getCurrentSteemUsername();
                    AccountName author = new AccountName(username);
                    Permlink permlink = new Permlink(__permlink);
                    boolean allowVotes = LocalConfig.ALLOW_VOTES;
                    boolean allowCurationRewards = LocalConfig.ALLOW_CURATION_REWARDS;
                    int percentSteemDollars = LocalConfig.PERCENT_STEEM_DOLLARS;
                    String jsonMetadata = new JsonMetaDataModel(tags, postStructure).getJson();
                    AccountName parentAuthor = null;
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
                    DynamicGlobalProperty globalProperties = steemJ.getDynamicGlobalProperties();
                    SignedTransaction signedTransaction = new SignedTransaction(globalProperties.getHeadBlockId(), operations, null);
                    signedTransaction.sign();
                    steemJ.broadcastTransaction(signedTransaction);
                    if (steemPostCreatorCallback != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                steemPostCreatorCallback.onPostCreatedOnSteem();
                                HaprampPreferenceManager.getInstance().setLastPostCreatedAt(System.currentTimeMillis());
                            }
                        }, 1000);
                    }
                } catch (final SteemCommunicationException e) {
                    e.printStackTrace();
                    if (steemPostCreatorCallback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                steemPostCreatorCallback.onPostCreationFailedOnSteem(e.getMessage());
                            }
                        });
                    }
                } catch (final SteemResponseException e) {
                    e.printStackTrace();
                    if (steemPostCreatorCallback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(e.getCode()==-32000) {
                                    steemPostCreatorCallback.onPostCreationFailedOnSteem("You can create only 1 post per 5 minute.");
                                }
                            }
                        });
                    }
                } catch (final SteemInvalidTransactionException e) {
                    e.printStackTrace();
                    if (steemPostCreatorCallback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                steemPostCreatorCallback.onPostCreationFailedOnSteem(e.getMessage());
                            }
                        });
                    }
                }
            }
        }.start();
    }

    private SteemPostCreatorCallback steemPostCreatorCallback;

    public void setSteemPostCreatorCallback(SteemPostCreatorCallback steemPostCreatorCallback) {
        this.steemPostCreatorCallback = steemPostCreatorCallback;
    }

    public interface SteemPostCreatorCallback {

        void onPostCreatedOnSteem();

        void onPostCreationFailedOnSteem(String msg);

    }

}
