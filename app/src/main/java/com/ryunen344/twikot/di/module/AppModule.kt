package com.ryunen344.twikot.di.module

import com.ryunen344.twikot.accountlist.AccountListFragment
import com.ryunen344.twikot.addTweetReply.AddTweetReplyContract
import com.ryunen344.twikot.addTweetReply.AddTweetReplyFragment
import com.ryunen344.twikot.addTweetReply.AddTweetReplyPresenter
import com.ryunen344.twikot.di.provider.AppProvider
import com.ryunen344.twikot.home.HomeActivity
import com.ryunen344.twikot.home.HomeContract
import com.ryunen344.twikot.home.HomeFragment
import com.ryunen344.twikot.home.HomePresenter
import com.ryunen344.twikot.home.tweet.HomeTweetContract
import com.ryunen344.twikot.home.tweet.HomeTweetFragment
import com.ryunen344.twikot.home.tweet.HomeTweetPresenter
import com.ryunen344.twikot.mediaViewer.MediaViewerActivity
import com.ryunen344.twikot.mediaViewer.MediaViewerContract
import com.ryunen344.twikot.mediaViewer.MediaViewerFragment
import com.ryunen344.twikot.mediaViewer.MediaViewerPresenter
import com.ryunen344.twikot.profile.ProfileActivity
import com.ryunen344.twikot.profile.ProfileContract
import com.ryunen344.twikot.profile.ProfileFragment
import com.ryunen344.twikot.profile.ProfilePresenter
import com.ryunen344.twikot.profile.tweet.ProfileTweetContract
import com.ryunen344.twikot.profile.tweet.ProfileTweetFragment
import com.ryunen344.twikot.profile.tweet.ProfileTweetPresenter
import com.ryunen344.twikot.profile.user.ProfileUserContract
import com.ryunen344.twikot.profile.user.ProfileUserFragment
import com.ryunen344.twikot.profile.user.ProfileUserPresenter
import com.ryunen344.twikot.settings.SettingsContract
import com.ryunen344.twikot.settings.SettingsFragment
import com.ryunen344.twikot.settings.SettingsPresenter
import com.ryunen344.twikot.settings.preferences.license.LicenseFragment
import com.ryunen344.twikot.settings.preferences.wallpaper.WallpaperPreferenceContract
import com.ryunen344.twikot.settings.preferences.wallpaper.WallpaperPreferenceDialogFragmentCompat
import com.ryunen344.twikot.settings.preferences.wallpaper.WallpaperPreferencePresenter
import com.ryunen344.twikot.tweetDetail.TweetDetailActivity
import com.ryunen344.twikot.tweetDetail.TweetDetailContract
import com.ryunen344.twikot.tweetDetail.TweetDetailFragment
import com.ryunen344.twikot.tweetDetail.TweetDetailPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val AppModule = module {
    single {AppProvider()}

    factory { AccountListFragment() }

    factory { HomeFragment() }
    scope(named<HomeFragment>()) {
        scoped { HomePresenter(getProperty(HomeActivity.INTENT_KEY_USER_ID)) as HomeContract.Presenter }
    }

    factory { HomeTweetFragment() }
    scope(named<HomeTweetFragment>()){
        scoped { HomeTweetPresenter(getProperty(HomeTweetFragment.INTENT_KEY_PAGER_POSITION), getProperty(HomeActivity.INTENT_KEY_USER_ID)) as HomeTweetContract.Presenter}
    }

    factory { AddTweetReplyFragment() }
    scope(named<AddTweetReplyFragment>()) {
        scoped { AddTweetReplyPresenter() as AddTweetReplyContract.Presenter }
    }

    factory { TweetDetailFragment() }
    scope(named<TweetDetailFragment>()) {
        scoped { TweetDetailPresenter(getProperty(TweetDetailActivity.INTENT_KEY_TWEET_ID)) as TweetDetailContract.Presenter }
    }

    factory { ProfileFragment() }
    scope(named<ProfileFragment>()) {
        scoped { ProfilePresenter(getProperty(ProfileActivity.INTENT_KEY_USER_ID), getProperty(ProfileActivity.INTENT_KEY_SCREEN_NAME)) as ProfileContract.Presenter }
    }

    factory { ProfileTweetFragment() }
    scope(named<ProfileTweetFragment>()) {
        scoped { ProfileTweetPresenter(getProperty(ProfileTweetFragment.INTENT_KEY_PAGER_POSITION), getProperty(ProfileActivity.INTENT_KEY_USER_ID), getProperty(ProfileActivity.INTENT_KEY_SCREEN_NAME)) as ProfileTweetContract.Presenter }
    }

    factory { ProfileUserFragment() }
    scope(named<ProfileUserFragment>()) {
        scoped { ProfileUserPresenter(getProperty(ProfileUserFragment.INTENT_KEY_PAGER_POSITION), getProperty(ProfileActivity.INTENT_KEY_USER_ID), getProperty(ProfileActivity.INTENT_KEY_SCREEN_NAME)) as ProfileUserContract.Presenter }
    }

    factory { MediaViewerFragment() }
    scope(named<MediaViewerFragment>()) {
        scoped { MediaViewerPresenter(getProperty(MediaViewerActivity.INTENT_KEY_MEDIA_URL)) as MediaViewerContract.Presenter }
    }

    factory { SettingsFragment() }
    scope(named<SettingsFragment>()) {
        scoped { SettingsPresenter() as SettingsContract.Presenter }
    }

    factory { WallpaperPreferenceDialogFragmentCompat() }
    scope(named<WallpaperPreferenceDialogFragmentCompat>()) {
        scoped { WallpaperPreferencePresenter() as WallpaperPreferenceContract.Presenter }
    }

    factory { LicenseFragment() }
//    scope(named<LicenseFragment>()){
//        scoped { LicensePresenter() as  }
//    }
}