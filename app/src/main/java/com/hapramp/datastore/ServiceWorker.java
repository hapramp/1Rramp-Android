package com.hapramp.datastore;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.hapramp.api.RetrofitServiceGenerator;
import com.hapramp.interfaces.datatore_callback.ServiceWorkerCallback;
import com.hapramp.preferences.CachePreference;
import com.hapramp.steem.Communities;
import com.hapramp.steem.ServiceWorkerRequestParams;
import com.hapramp.steem.models.Feed;
import com.hapramp.steem.models.FeedResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceWorker {

  private static final String TAG = ServiceWorker.class.getSimpleName();
  private static final long DELAY_IN_CACHE_RESPONSE = 500;
  ServiceWorkerCallback serviceWorkerCallback;
  private Handler mHandler;
  private CachePreference mCachePreference;
  private ServiceWorkerRequestParams currentRequestParams;

  public void init(Context context) {
    mCachePreference = CachePreference.getInstance();
    mHandler = new Handler();
  }

  public void setServiceWorkerCallback(ServiceWorkerCallback serviceWorkerCallback) {
    this.serviceWorkerCallback = serviceWorkerCallback;
  }

  public void requestAllFeeds(final ServiceWorkerRequestParams requestParams) {

    this.currentRequestParams = requestParams;
    if (mCachePreference.isFeedResponseCached()) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse cachedFeedResponse = mCachePreference.getCachedFeedResponse();
          mHandler.post(new Runnable() {
            @Override
            public void run() {
              if (serviceWorkerCallback != null) {
                serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cachedFeedResponse.getFeeds(), cachedFeedResponse.getLastAuthor(), cachedFeedResponse.getLastPermlink());
              }
            }
          });
        }
      }.start();
      fetchAllFeeds(requestParams);
    } else {
      fetchAllFeeds(requestParams);
    }
  }

  private void fetchAllFeeds(final ServiceWorkerRequestParams feedRequestParams) {
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    RetrofitServiceGenerator.getService()
      .getUserFeeds(feedRequestParams.getUsername(), feedRequestParams.getLimit())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
          if (isRequestLive(feedRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                new Thread() {
                  @Override
                  public void run() {
                    mCachePreference.cacheFeedResponse(response.body());
                  }
                }.start();
                if (!isRequestForCommunityFeed(feedRequestParams)) {
                  serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
                } else {
                  new Thread() {
                    @Override
                    public void run() {
                      mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                          List<Feed> filteredFeeds = FeedsFilter.filter(response.body().getFeeds(), currentRequestParams.getCommunityTag());
                          if (filteredFeeds.size() > 0) {
                            serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) filteredFeeds, response.body().getLastAuthor(), response.body().getLastPermlink());
                          } else {
                            serviceWorkerCallback.onNoDataAvailable();
                          }
                        }
                      });
                    }
                  }.start();
                }
              } else {
                serviceWorkerCallback.onFetchingFromServerFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onFetchingFromServerFailed();
          }
        }
      });
  }

  private boolean isRequestLive(ServiceWorkerRequestParams serviceWorkerRequestParams) {
    return serviceWorkerRequestParams.equals(currentRequestParams);
  }

  private boolean isRequestForCommunityFeed(ServiceWorkerRequestParams serviceWorkerRequestParams) {
    return !serviceWorkerRequestParams.getCommunityTag().equals(Communities.ALL);
  }

  public void requestCommunityFeeds(final ServiceWorkerRequestParams requestParams) {
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onLoadingFromCache();
    }
    this.currentRequestParams = requestParams;
    if (mCachePreference.isFeedResponseCached()) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse feedResponse = mCachePreference.getCachedFeedResponse();
          if (isRequestLive(requestParams)) {
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                List<Feed> filteredFeeds = FeedsFilter.filter(feedResponse.getFeeds(), currentRequestParams.getCommunityTag());
                if (filteredFeeds.size() > 0) {
                  serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) filteredFeeds, feedResponse.getLastAuthor(), feedResponse.getLastPermlink());
                }
              }
            });
          }
        }
      }.start();
      fetchAllFeeds(requestParams);
    } else {
      fetchAllFeeds(requestParams);
    }
  }

  public void requestAppendableFeed(final ServiceWorkerRequestParams requestParams) {
    this.currentRequestParams = requestParams;
    RetrofitServiceGenerator.getService().getUserFeeds(
      requestParams.getUsername(),
      requestParams.getLimit(),
      requestParams.getLastAuthor(),
      requestParams.getLastPermlink()).enqueue(new Callback<FeedResponse>() {
      @Override
      public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
        if (isRequestLive(requestParams)) {
          if (serviceWorkerCallback != null) {
            if (response.isSuccessful()) {
              if (!isRequestForCommunityFeed(requestParams)) {
                serviceWorkerCallback.onAppendableDataLoaded(response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                List<Feed> filteredFeeds = FeedsFilter.filter(response.body().getFeeds(), currentRequestParams.getCommunityTag());
                serviceWorkerCallback.onAppendableDataLoaded(filteredFeeds, response.body().getLastAuthor(), response.body().getLastPermlink());
              }
            } else {
              serviceWorkerCallback.onAppendableDataLoadingFailed();
            }
          }
        }
      }

      @Override
      public void onFailure(Call<FeedResponse> call, Throwable t) {
        if (isRequestLive(requestParams)) {
          if (serviceWorkerCallback != null) {
            serviceWorkerCallback.onAppendableDataLoadingFailed();
          }
        }
      }
    });
  }

  public void requestTrendingPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    if (mCachePreference.isTrendingCached()) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse cfr = mCachePreference.getTrendingFeedResponseFromCache();
          mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              if (serviceWorkerCallback != null) {
                if (cfr != null) {
                  serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cfr.getFeeds(), cfr.getLastAuthor(), cfr.getLastPermlink());
                }
              }
            }
          }, DELAY_IN_CACHE_RESPONSE);
        }
      }.start();
    }

    RetrofitServiceGenerator.getService().getTrendingFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                new Thread() {
                  @Override
                  public void run() {
                    mCachePreference.saveTrendingCacheAsJson(new Gson().toJson(response.body()));
                    mCachePreference.setTrendingCached(true);
                  }
                }.start();
                serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onFetchingFromServerFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestAppendableFeedForTrending(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    RetrofitServiceGenerator.getService().getTrendingFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit(), serviceWorkerRequestParams.getLastAuthor(), serviceWorkerRequestParams.getLastPermlink())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                serviceWorkerCallback.onAppendableDataLoaded(response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onAppendableDataLoadingFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestHotPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    if (mCachePreference.isHotCached()) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse cfr = mCachePreference.getHotFeedResponseFromCache();
          mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              if (serviceWorkerCallback != null) {
                if (cfr != null) {
                  serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cfr.getFeeds(), cfr.getLastAuthor(), cfr.getLastPermlink());
                }
              }
            }
          }, DELAY_IN_CACHE_RESPONSE);
        }
      }.start();
    }
    RetrofitServiceGenerator.getService().getHotFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                new Thread() {
                  @Override
                  public void run() {
                    mCachePreference.saveHotCacheAsJson(new Gson().toJson(response.body()));
                    mCachePreference.setHotCached(true);
                  }
                }.start();
                serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onFetchingFromServerFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestAppendableFeedForHot(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    RetrofitServiceGenerator.getService().getHotFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit(), serviceWorkerRequestParams.getLastAuthor(), serviceWorkerRequestParams.getLastPermlink())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                serviceWorkerCallback.onAppendableDataLoaded(response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onAppendableDataLoadingFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestLatestPosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {

    this.currentRequestParams = serviceWorkerRequestParams;

    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    if (mCachePreference.isLatestCached()) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse cfr = mCachePreference.getLatestFeedResponseFromCache();
          mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              if (serviceWorkerCallback != null) {
                if (cfr != null) {
                  serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cfr.getFeeds(), cfr.getLastAuthor(), cfr.getLastPermlink());
                }
              }
            }
          }, DELAY_IN_CACHE_RESPONSE);
        }
      }.start();
    }
    RetrofitServiceGenerator.getService().getLatestFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                new Thread() {
                  @Override
                  public void run() {
                    mCachePreference.saveLatestCacheAsJson(new Gson().toJson(response.body()));
                    mCachePreference.setLatestCached(true);
                  }
                }.start();
                serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onFetchingFromServerFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestAppendableFeedForLatest(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    RetrofitServiceGenerator.getService().getLatestFeed(serviceWorkerRequestParams.getCommunityTag(), serviceWorkerRequestParams.getLimit(), serviceWorkerRequestParams.getLastAuthor(), serviceWorkerRequestParams.getLastPermlink())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                serviceWorkerCallback.onAppendableDataLoaded(response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onAppendableDataLoadingFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestProfilePosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    if (mCachePreference.isProfilePostCached(serviceWorkerRequestParams.getUsername())) {
      new Thread() {
        @Override
        public void run() {
          final FeedResponse cfr = mCachePreference.getProfileCachedPost(serviceWorkerRequestParams.getUsername());
          mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
              if (serviceWorkerCallback != null) {
                if (cfr != null) {
                  serviceWorkerCallback.onLoadedFromCache((ArrayList<Feed>) cfr.getFeeds(), cfr.getLastAuthor(), cfr.getLastPermlink());
                }
              }
            }
          }, DELAY_IN_CACHE_RESPONSE);
        }
      }.start();
    }
    RetrofitServiceGenerator.getService().getPostsOfUser(serviceWorkerRequestParams.getUsername(), serviceWorkerRequestParams.getLimit())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, final Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                new Thread() {
                  @Override
                  public void run() {
                    mCachePreference.saveProfileCacheAsJson(serviceWorkerRequestParams.getUsername(), new Gson().toJson(response.body()));
                    mCachePreference.setProfilePostCached(serviceWorkerRequestParams.getUsername(), true);
                  }
                }.start();
                serviceWorkerCallback.onFeedsFetched((ArrayList<Feed>) response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onFetchingFromServerFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }

  public void requestAppendableProfilePosts(final ServiceWorkerRequestParams serviceWorkerRequestParams) {
    this.currentRequestParams = serviceWorkerRequestParams;
    if (serviceWorkerCallback != null) {
      serviceWorkerCallback.onFetchingFromServer();
    }
    RetrofitServiceGenerator.getService().getPostsOfUser(serviceWorkerRequestParams.getUsername(), serviceWorkerRequestParams.getLimit(), serviceWorkerRequestParams.getLastAuthor(), serviceWorkerRequestParams.getLastPermlink())
      .enqueue(new Callback<FeedResponse>() {
        @Override
        public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
          if (isRequestLive(serviceWorkerRequestParams)) {
            if (serviceWorkerCallback != null) {
              if (response.isSuccessful()) {
                serviceWorkerCallback.onAppendableDataLoaded(response.body().getFeeds(), response.body().getLastAuthor(), response.body().getLastPermlink());
              } else {
                serviceWorkerCallback.onAppendableDataLoadingFailed();
              }
            }
          }
        }

        @Override
        public void onFailure(Call<FeedResponse> call, Throwable t) {
          serviceWorkerCallback.onFetchingFromServerFailed();
        }
      });
  }


}
