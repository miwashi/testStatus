package net.miwashi.receiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;

public class UDPClient {

    private Log log = LogFactory.getLog(UDPClient.class);

    @Value("${configuration.log.sendtestdata:false}")
    private boolean do_log_sent_testdata = false;

    @Value("${configuration.udp.port:6500}")
    private int udpPort = 6500;

    @Value("${configuration.udp.host:localhost}")
    private String udpHost = "localhost";
    
    
    private static String[][] DATA_BUILD = new String[][]{
    	{"6760","2015-08-09_16-58-04","https://barnjenkins.svti.svt.se​/job/escenic-barnkanalen--regressiontest/6760/","https://barnjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6760","barnplay-regressiontest"}
    	,{"6761","2015-08-09_16-58-05","https://nssjenkins.svti.svt.se​/job/escenic-nss--regressiontest/6761/","https://nssjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6761","nss-regressiontest"}
    	,{"6762","2015-08-09_16-58-06","https://programjenkins.svti.svt.se​/job/escenic-program--regressiontest/6762/","https://programjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6762","program-regressiontest"}
    	,{"6763","2015-08-09_16-58-07","https://sportjenkins.svti.svt.se​/job/escenic-sport--regressiontest/6763/","https://sportjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6763","sport-regressiontest"}
    	,{"6764","2015-08-09_16-58-08","https://barnjenkins.svti.svt.se​/job/escenic-barnkanalen--regressiontest/6764/","https://barnjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6764","barn-regressiontest"}
    	,{"6765","2015-08-09_16-58-09","https://atvjenkins.svti.svt.se​/job/escenic-atv--regressiontest/6765/","https://atvjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6765","atv-regressiontest"}
    	,{"6766","2015-08-09_16-58-10","https://atvjenkins.svti.svt.se​/job/escenic-atv--regressiontest/6766/","https://atvjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6766","atv-regressiontest-nodata"}
    	,{"6767","2015-08-09_16-58-11","https://nssjenkins.svti.svt.se​/job/escenic-nss--regressiontest/6767/","https://nssjenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6767","nss-regressiontest-nodata"}
    	,{"6768","2015-08-09_16-58-12","https://svtsejenkins.svti.svt.se​/job/escenic-svtse--regressiontest/6768/","https://svtsejenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6768","svtse-regressiontest"}
    	,{"6769","2015-08-09_16-58-13","https://svtsejenkins.svti.svt.se​/job/escenic-svtse--regressiontest/6769/","https://svtsejenkins.svti.svt.se​/","master","jenkins-escenic-barnkanalen--regressiontest-6769","svtse-regressiontest-nodata"}
    };

	private static String[][] DATA = new String[][]{
		{"se.svt.test.atv.widgets.FooterTest.shouldHaveClickableLogoThatDirectsToSvtFirstPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveEpisodePlayer","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveChef","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTabMenu","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveXSmallMenuWithFiveNavigationElements","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchShouldReturnAtLeastOneValidResult","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldShowNewTitlesWhenClickingOnArrows","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatLiveLabelExists","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.shouldHaveLinksInFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldBePresentAndDisplayedWithInfoAndImage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldHaveTwoVisibleArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldShow404PageWhenExpiredEpisode","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifySectionBasedPlayerContainsEightThumbnailsAndAnAnchorId","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveCookingInfo","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.performingASearchShouldReturnNonEmptyList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldHaveTwoVisibleArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveGlobalFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveNavigationButtons","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldHaveSchedulePageShowingScheduleForAtLeastTodayAndTomorrow","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldBeAbleToLoadMorePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveExpandablePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.PersonalizingTest.shouldCenterLastWatchedTitleInCharacterList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveLogo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatStartContainsLiveBroadcast","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.FooterTest.shouldHaveClickableLogoThatDirectsToSvtFirstPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveAsideContainerWithArticlesOnStartPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldBelongToAProgram","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TablaTest.shouldShowTheMenuAndTheSchedule","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchShouldReturnNonEmptyResult","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.VTmaTest.allTestsOnTvTabla","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveCharacterListListDisplayed","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldUpdateHistoryListWhenWatchingMultipleVideos","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveGlobalFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theCategorySportUrlShouldContainSportTitles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldHaveMoreMenuInDimensionXS","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatFutureBroadcastHasButtonsForNextEpisodes","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldShowMoreArticlesWhenPressedMoreArticlesButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeTwoOverTwoTheSameHorizontalInXSmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeInstagram","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldHaveVideoPosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldHaveCustomizedPageTitle","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveFourNavigationIcons","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldVerifyThatTeaserHasEpisodeTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowVotingOptionsOrderedByVotes","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveAsideContainerWithArticlesOnStartPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldBeAbleToLoadMorePosts","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveCharacterImagesPresent","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectNumberOfBlocksOnTestedPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theViewAllaShouldIncludeBothActiveAndInactiveLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldNotHaveHistoryListFromBeginning","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBePostInPost","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldNotBeDisplayedFromTheBeginning","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldDisplayEpisodeTVGuide","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeYoutube","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatProgramLogoIsClickableAndLeadsToStartPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatTopAreaContainsAFeaturedTop","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHavePosts","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveCorrectDivs","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldJustHaveOneNavigationElementIfNotActiveShow","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldReturnFooterContainingThreeLogos","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveSlowFlow","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SjuanSingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldStayOnPageIfNoInputAndHitReturn","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveArrowInScreamerXS","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveBlockNavigation","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchUsingTheSearchPageSearchBoxShouldGenerateHits","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldDisplayInfoRegardingExpireDate","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveFeaturedTopSectionWithArticles","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldBeAbleToPostACommentWhenModeratingIsOn","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowBigIconOnVote","FAIL","firefox","linux"}
		,{"se.svt.test.atv.global.Http404Test.shouldHave404WithMenuAndFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderLinkToCraftsPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldHaveGeoBlockContent","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldShowTimeToPrepare","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Svt1SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveSlowFlow","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldSetFocusOnFirstSearchHitWhenSearchFieldIsActiveAndUserHitsEnter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.FourOFourPageTest.shouldNotBeDisplayedOnStartPageFromTheBeginning","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldBelongToAProgram","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldDisplayToday","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.TaggLopTest.verifyExistenceOfTagglopLinksInArticleAndSeparateTaggLop","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Kanal5SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveCorrectDivs","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveFooterWithLogos","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldHaveSearchField","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldDisplayInfoRegardingExpireDate","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaSpelhusetTeaserTest.verifyOneTeaserGroup","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTitleInfoOnPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.humorShouldHaveAllRelatedSitesSection","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveATwoColumnBlock","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeTwoOverTwoTheSameHorizontalInSmall","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveAsideContainerWithArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldHaveCorrectNumberOfTitlesVisibleInList","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.LoadMetricsTest.shouldLoadPlayStartPageUnder5Sec","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveFooter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatFutureBroadcastHasButtonsForPrevEpisodes","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldHaveExpandableVotingOptionsIfInactive","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv6SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.defaultViewShouldIncludeActiveLinksAndNoInactiveLinks","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderWithNavigationElements","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatHeadlineReflectsLive","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveSocialMediaLinks","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHavePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldDisplayEpisodeTVGuide","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchInputShouldNotBeVisibleInSmallMode","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.verifyArticleView","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldHaveAMoreArticlesButtonWhenTenOrMoreArticlesInList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyLocalHeaderMinHeight","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnStartPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldDisplayCorrectNumberOfPosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatSideAreaContainsArticles","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveLinkToNextPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveLocalMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifyAllVideoPlayersAreRendered","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.SvtWorldTest.shouldKeepTimeOffsetWhenNavigatingBetweenDays","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyArticleListElementIsAbleToExpandAndClose","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalAutocompleteTest.autoCompleteExistAndLinksAreCorrect","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TablaTest.shouldShowTheMenuAndTheSchedule","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveLinkToEnglishTranslation","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchUsingTheHeaderSearchFieldShouldGenerateHits","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveFiveHeaderIconsInXSmall","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveHeaderWithImageNameAndIngress","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Svt2SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoPlayerTest.shouldBePossibleOnTitlePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveRelatedObjects","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.SvtWorldTest.shouldKeepTimeOffsetWhenNavigatingBetweenDays","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.BarnkanalenSingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveIngredients","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.footerShouldExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.MoreOnSvtAreaTest.moreOnSvtAreaShouldWorkAccordingToSpec","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTextBlockOnThePage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveMenuWithSevenTabs","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TeaserBoxBarnTest.shouldShowPopUpOnClick","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveCookingInfo","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveALinkToVideoSectionWithNrOfEpisodesInLinkText","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveMainSectionWithArticles","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldHaveThumbnails","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldChangeActiveTitleInSearchResultListWithTabKey","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.SvtWorldTest.shouldUpdateTvTableAccordingToSelectedTimeOffset","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHavePlayButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveClickableMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldReturnFooterContainingThreeLogos","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyThatArticleGroupExists","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldDisplayCategoryIcons","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldHaveDiscussions","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowCorrectVignette","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv3SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveCookingInfo","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldJustHaveOneNavigationElementIfNotActiveShow","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsSectionShouldDisplaySevenMediumOrSevenMediumAndOneLargeOrFiveMediumAndTwoLargeTeasers","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveFooter","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsArrowsShouldPointToNews","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldGiveErrorMessageWhenSearchingOnNonExistingTitles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldEmptySearchFieldWhenSearchFieldIsActiveAndUserHitsEscape","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeCloseable","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.performingASearchShouldReturnNonEmptyList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldGiveErrorMessageWhenAlreadyVoted","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveHeader","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveLinkBackToBarnkanalen","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveMainSectionWithArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldHaveSearchField","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatFutureBroadcastHasButtonsForNextEpisodes","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldShowTenRecipes","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnAppIcons","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveLinksToTitlePagesOnBarnkanalen","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeCloseable","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnOpinionsPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowBigIconOnVote","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedVideoThatCanPlayVideo","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SvtwSingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveTopTeaser","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldLinkToEpisodeModal","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayThreeCompleteChannelsInLarge","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.humorShouldHaveAllRelatedSitesSection","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldHaveTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveBigHeader","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveTextPresentUnderCenteredTitle","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveATwoColumnBlock","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveMenuAndNavigationBar","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeOnTheSameHorizontalInLarge","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.programAoShouldHaveTeasersOnTop","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldHaveButtonInHeaderInDimensionSmallAndXsmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeOnTheSameHorizontalInMedium","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.aoShouldHaveAnchorTargetsForAllElementsInNavigationList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldShowTimeToPrepare","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBePostInPost","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.theListingComesUpWithMenu","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.AnchorLinkTest.testThatOneTeaserCanBeAnchorLinkedToAnotherTeaserOnTheSamePage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectStyleOnThirdBlock","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldShiftCharactersWhenClickingOnArrows","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldHaveAMoreArticlesButtonWhenTenOrMoreArticlesInList","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveSearchField","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatHeadlineReflectsLive","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeTwoOverTwoTheSameHorizontalInXSmall","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTextBlockOnThePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHavePosts","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleLiveEventTest.shouldShowPoll","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveOpinionTeaser","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldUpdateHistoryListWhenWatchingMultipleVideos","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.FeaturedTopTest.shouldVerifyThatFeaturedTopContainsArticles","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveCssForAChannelIconPresent","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTabMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.AnchorLinkTest.testThatOneTeaserCanBeAnchorLinkedToAnotherTeaserOnTheSamePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldBelongToAProgram","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnHeadIncludes","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldNotBeAbleToPostACommentIfMessageIsNotEntered","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldHaveAClickableMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveMainContainerWithArticlesOnNewsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveHeaderWithImageVignetteAndHeaders","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.MedaljligaTest.shouldBeVisible","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldNotBeDisplayedFromTheBeginning","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldShowMoreRecipesWhenClickingOnMoreButton","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveLinkToEnglishTranslation","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TeaserBoxBarnTest.shouldShowPopUpOnClick","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveBackgroundTheme","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldHaveButtonInHeaderInDimensionSmallAndXsmall","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldExpandXSmallMoreMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveAsideContainerWithArticlesOnNewsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldExpandXSmallMoreMenu","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveCorrectText","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHaveImage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsSectionShouldDisplaySevenMediumOrSevenMediumAndOneLargeOrFiveMediumAndTwoLargeTeasers","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldNotHaveHistoryListFromBeginning","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderLinkToCraftsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveFiveHeaderIconsInXSmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldHaveSearchAlternatives","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldShowMoreThanOneArticleButMaximumTenArticlesOnOnePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldVerifyThatTeaserIsPresent","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveClickableMenu","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldHaveCommentFieldBetweenStuckAndPost","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatProgramLogoIsClickableAndLeadsToStartPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.smallImagesShouldBeAlignedToTheLeft","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyArticleListElementIsAbleToExpandAndClose","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldHaveCommentFieldBetweenStuckAndPost","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldShow404PageWhenNonExistingEpisodeId","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowCorrectVignette","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveMainContainerWithArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FourOFourPageTest.shouldBeDisplayedWhenIncorrectUrlIsEnteredAndShouldBeCloseable","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifyAreaBasedPlayer","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.footerShouldHaveFourLinkColumns","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FourOFourPageTest.shouldNotBeDisplayedOnStartPageFromTheBeginning","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldBeActiveOrClosedAndOpenOrHidden","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldOnlyHaveShareInfoInSmallMode","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldBeAbleToSwitchBetweenTabs","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaTitlePageTest.shouldHaveMenuLinkToAllRelatedSites","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveFeaturedTopOnNewsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatLiveLabelExists","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatATitlePageHasFutureBroadcasts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldShowMoreRecipesWhenClickingOnMoreButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldLeadToCorrectPageAfterClickOnVideoSectionLink","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveContentListTopWithArticle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveArchivedPostsInRightColumn","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveAClickableMenu","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWhenChoosingCheckBoxAlternatives","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldReturnOneNavigationElementWithThreeListItems","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldGiveErrorMessageWhenAlreadyVoted","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldExistOnAllTypesOfPages","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldShowTenRecipes","SUCCESS","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHavePersonImage","FAIL","firefox","any"}
		,{"se.svt.test.svtse.GlobalSearchTest.shouldExpandInputFieldWhenClickOnSearch","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.defaultViewShouldIncludeActiveLinksAndNoInactiveLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeOnTheSameHorizontalInLarge","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldHaveLinkToPollResult","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.programAoShouldHaveTeasersOnTop","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveBigHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldShowPollResults","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchTermShouldBeHighlighted","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHavePosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveContentListTopWithArticle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldHaveTheTextSverigesTelevisionInTheFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.theListingComesUpWithFourColumns","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchForStringWithoutHitsShouldReturnNoHitsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SvtkSingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.noMoreThanThreePastEntriesShouldBeVisible","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv6SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnDocTypeData","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleLiveEventTest.shouldHaveNewestPostFirst","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldShowStatisticsOfInFooterWhenImageAndInAnArticle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatHeaderElementsExist","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHomeLinkThatNavigatesBackToFrontPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.GlobalAutocompleteTest.autoCompleteNoResultShouldShowUp","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveVideoClipPlayer","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldHaveAClickableMenu","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldHaveSearchAlternatives","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldHaveEqualNumberOfLargeImagesAsThumbnails","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldChangeActiveImageAndLargeDisplayedImageOnThumbnailClick","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveShareInfo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnOpinionsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveALinkToVideoSectionWithNrOfEpisodesInLinkText","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayThreeCompleteChannelsInLarge","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldStayOnPageIfNotFoundAndHitReturn","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnAskUsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldChangeThumbnailsInCarouselWhenClickingOnArrows","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowTotalNumberOfVotes","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveChef","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatATitlePageHasFutureBroadcasts","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveXSmallMenuWithFiveNavigationElements","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.regionalLinkAtTopShouldHaveCorrectDestination","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeWikipedia","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldVerifyArticleListElementIsAbleToExpandAndClose","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectStyleOnThirdBlock","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveGridWithCurrentTitles","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPlayerInFeedTest.shouldHaveVideoPlayerWithCorrectAttributes","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SlowFlowTest.shouldDisplayCorrectNumberOfPosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.theListingComesUpWithFourColumns","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveNavigationButtons","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnHeadIncludes","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveCharacteristics","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectNumberOfBlockNavigationElements","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderLinkToGamesPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderWithNavigationElements","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldExpandXSmallMoreMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveAnalysisTeaser","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderLinkToCraftsPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldBePresentOnAllTypesOfPages","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaURLTest.shouldRedirectToBolibompaWithTrailingSlash","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatAHitHasADateAndAnIconForChannel","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldOnlyHaveShareInfoInSmallMode","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveWideviewTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveSwipeTeasers","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.AlphabeticalIndexPageTest.shouldExistForGames","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowVotingResultsWhenClosed","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedList","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.videoPlayerWithOnlyOneVideoShouldShowNoVideoList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveAsideContainerWithArticlesOnNewsPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldReturnOneNavigationElementWithThreeListItems","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldBeActiveOrClosedAndOpenOrHidden","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationWithoutJavascriptShouldWork","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.LoadMetricsTest.shouldLoadPlayStartPageUnder5Sec","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldVerifyThatTeaserIsPresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveGlobalFooter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHavePossibilityToPlayVideoInFeed","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveLinkBackToBarnkanalen","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveLogoInHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveHiddenDescriptionThatIsDisplayedWhenClickedOn","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.noMoreThanThreePastEntriesShouldBeVisible","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.shouldOnlyHaveOneFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveRelatedObjects","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatTextLogoIsClickableAndLeadsToStartPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveAOneColumnBlock","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.ScribbleLiveHeaderTest.templateShouldHaveProperContainers","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Kanal9SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldExist","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldContainVideoPlayer","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveAClickableMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldShowPollResults","SUCCESS","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHaveHeadline","SUCCESS","firefox","any"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveLinksToTitlesInBarnplay","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifyAllVideoPlayersAreRendered","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaTitlePageTest.shouldHaveBottomSection","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleLiveEventTest.shouldShowPoll","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInMedium","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveHeaderWithImageNameAndIngress","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectTitleOnSecondBlock","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldReturnMenuItemsUsingTheWidgetDispatcher","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWithTextSearch","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.ProgramInfoTest.linksAndMoreProgramInformationShouldBeVisible","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldShowSearchFieldWhenClickingOnSearchIcon","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldVerifyArticleListElementIsAbleToExpandAndClose","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsTopImageShouldBeCentered","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHaveImage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.shouldOnlyHaveOneFooter","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.AlphabeticalIndexPageTest.shouldExistForGames","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveExpandablePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveScreamerWithTextAndLink","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveRelatedContent","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveSvtHeader","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveRssSubscribeButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldHaveOldestPostFirst","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWhenChoosingDropdownAlternatives","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldShowMoreThanOneArticleButMaximumTenArticlesOnOnePage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveSvtHeader","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveFeaturedTopSectionWithArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FrameworkTest.shouldHaveFrameworkBlocksOnATitlePage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveSearchField","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FourOFourPageTest.shouldBeDisplayedWhenIncorrectUrlIsEnteredAndShouldBeCloseable","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaTitlePageTest.shouldHaveBottomSection","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowVotingResultsWhenClosed","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldHaveKunskapskanalenTitleSuffix","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveHeaderWithImageVignetteAndHeaders","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.shouldExpandInputFieldWhenClickOnSearch","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.ProgramInfoTest.programInfoShouldExpandAndCloseOnClick","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTeaserGroup","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv8SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldOpenArticleWhenClicked","FAIL","firefox","any"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveSvtHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SlowFlowTest.shouldDisplayCorrectNumberOfPosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWhenChoosingDropdownAlternatives","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldHaveTitle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatClickOnNextButtonLoadsMoreElementsToPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldShowNumberOfPortions","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.ProgramInfoTest.linksAndMoreProgramInformationShouldBeVisible","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeTwoOverTwoTheSameHorizontalInSmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveWorkingNavigationBar","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldHaveLoadMoreButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnViewPort","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldHaveTitle","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompahusetTeaserTest.verifyTwoPlusOneTeaserGroup","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsVideoPlayerTest.verifyClipPlayerExistence","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldShowNewTitlesWhenClickingOnArrows","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveCharacteristics","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HeaderTest.shouldHaveFourNavigationIcons","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyThatFooterExists","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveLocalMenu","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldHaveInputFieldInHeaderInDimensionLargeAndMedium","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldHaveNumberOfPicturesWrittenInFooterWhenNotStandalone","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.PostTest.shouldHaveContent","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleLiveEventTest.shouldHaveNewestPostFirst","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.urLinksShouldBeValid","FAIL","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyThatArticleGroupExists","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaURLTest.shouldNotRedirectForBolibompaSubsections","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldHaveTitleListsDisplayed","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.clickingOnAnchorLinksShouldTakeUsToTheAnchorTarget","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldHaveMoreButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldBeAbleToLoadMorePosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveIngredients","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveRadiohjalpenTitleSuffix","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWhenChoosingCheckBoxAlternatives","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveLocalMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInXSmall","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.tablaPageShouldContainSearchBox","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchForStringWithoutHitsShouldReturnNoHitsPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldHaveVideoPlayerAndChangeImageWhenClickingOnIt","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveEpisodePlayer","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveTopModuleWithFeaturedArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldDisplayCorrectNumberOfPosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.regionalLinkAtTopShouldHaveCorrectDestination","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SvtwSingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaURLTest.shouldNotRedirectForBolibompaSubsections","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveLinkToNextPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.footerShouldExist","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveCssForAChannelIconPresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHavePlayerWithCorrectAttributes","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveMoreButtonOnSmallScreens","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldDisplayCorrectNumberOfPosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveIngredients","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatClickOnPrevButtonLoadsMoreElementsToPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldNotBeAbleToPostACommentIfMessageIsNotEntered","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldShowCaptionTextInFooterWhenVideoAndInAnArticle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldNotHaveCarouselPresentWhenAllImagesAreShownFromTheBeginning","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveAllChannelsInMainView","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatClickOnNextButtonLoadsMoreElementsToPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderLinkToGamesPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldHaveOnlyOneActiveVideoRightsIcon","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveSocialMediaLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatTopAreaContainsAFeaturedTop","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveLinkToSchedulePage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Kanal5SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldShow404PageWhenNonExistingEpisodeId","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveRadiohjalpenTitleSuffix","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FooterTest.shouldHaveFourClickableLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.ProgramInfoTest.programInfoShouldExpandAndCloseOnClick","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.linkColumnsShouldBeOnTheSameHorizontalInMedium","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchUsingTheSearchPageSearchBoxShouldGenerateHits","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeDisplayedWhenClickingOnEpisodeInListUnderCategoryList","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldNotBeAbleToPostACommentIfUsernameIsNotEntered","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaOpenInNewWindowTest.openBolibompaInNewWindow","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldBePresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldContainVideoPlayer","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTitleRights","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveVideoPlayerThatCanPlayVideo","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveTextPresentUnderSomeOfTheTitles","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHaveTeaserPlayStamp","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldLoadMoreArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theCategorySportUrlShouldContainSportTitles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldOpenBolibompaInNewWindow","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveCorrectText","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveLocalMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldHaveMoreMenuInDimensionXS","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveTags","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompahusetTeaserTest.verifyOnePlusTwoTeaserGroup","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsVideoPlayerTest.clickVideoThumbnailAndVerifyThatCorrectVideoIsStartedInClipPlayer","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldHavePollWithPossibilityToVoteIn","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveBackgroundTheme","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.escenic.TreeChopper.performTreeChop","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldShowTitleListWhenSearchingOnTitles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldGivePossibilityToVote","FAIL","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldVerifyThatFooterExists","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldBeAbleToLoadMorePosts","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnViewPort","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTeaserGroup","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchShouldReturnAtLeastOneValidResult","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldLoadMoreArticles","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.allTeasersShouldHaveClickableTitlesAndTextBodies","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeDisplayedWhenClickingOnEpisodeInListUnderCategoryList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveTopModuleWithFeaturedArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldDisplayToday","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.viewingListingFromTodayShouldDisplayAsideListings","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldHaveOnlyOneActiveVideoRightsIcon","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationButtonsShouldExist","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theCategoryBarnUrlShouldNotContainSportTitles","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchShouldReturnNonEmptyResult","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveShareInfo","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.footerShouldHaveFourLinkColumns","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldShiftCharactersWhenClickingOnArrows","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectTitleOnSecondBlock","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveLogoInHeader","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveAsideContainerWithArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.AlphabeticalIndexPageTest.shouldExistForAllPrograms","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldContainFourVideoRightsIcons","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeDisplayedWhenClickingOnEpisodeInListUnderCharacterList","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeFacebook","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowVotingOptionsOrderedByVotes","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveCharacterList","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveFooter","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldHaveLinkToPollResult","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeTwitter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnFindAnswersPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsTopImageShouldBeCentered","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldShowTimeToPrepare","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveAllRelatedSitesSection","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveAnalysisTeaser","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.FeaturedTopTest.shouldVerifyThatFeaturedTopContainsArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatTextLogoIsClickableAndLeadsToStartPage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInXSmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveMoreButton","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalAutocompleteTest.autoCompleteExistAndLinksAreCorrect","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHavePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldContainFourVideoRightsIcons","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveTopTeaserWithImageAndText","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldHaveHiddenDescriptionThatIsDisplayedWhenClickedOn","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveCharacterList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveRelatedObjects","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldShowNumberOfPortions","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowImageForVotingOptions","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.ShowMoreArticlesTest.shouldShowMoreArticlesWhenPressedMoreArticlesButton","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv3SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatClickOnPrevButtonLoadsMoreElementsToPage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveSwipe","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveAOneColumnBlock","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.verifyArticleView","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHaveTeaserPlayStamp","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveScreamerWithTextAndLink","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.viewingListingFromYesterdayShouldHideAsideListings","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldExistOnAllTypesOfPages","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeSearchTest.shouldBePossibleWithTextSearch","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeFlickr","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FooterTest.shouldHaveFourClickableLinks","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveTitleInfoOnPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldHaveTheTextSverigesTelevisionInTheFooter","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldToggleSearchFieldToSearchIconInSmallWindowMode","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FooterTest.shouldHaveSVTPresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldShowNumberOfPortions","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv4SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeFacebook","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.EpisodeTest.verifyThatStartContainsLiveBroadcast","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveWorkingNavigationBar","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.aoShouldHaveAnchorTargetsForAllElementsInNavigationList","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.aoShouldHaveNavigationList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveLinkToSchedulePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveExpandablePosts","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.nonsenseSearchShouldReturnEmptyResult","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveCharacterListListDisplayed","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.VTmaTest.allTests","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTVGuide","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldHaveMoreThanOneVotingOption","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleArchivedEventTest.shouldHaveOldestPostFirst","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveExpandablePosts","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.theListingComesUpWithMenu","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchInputShouldNotBeVisibleInSmallMode","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveIconImageInBlockHeader","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldReturnMenuItemsUsingTheWidgetDispatcher","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.headerShouldExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatMainAreaContainsArticleList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveChef","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaURLTest.shouldRedirectToBolibompaWithTrailingSlash","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldBeClickableAndStartVideo","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveFastFlow","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveFeaturedTopOnNewsPage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldHaveDiscussions","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeWikipedia","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldDisplayCorrectNumberOfPosts","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationShouldWork","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SvtkSingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderWithNavigationElements","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInMedium","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldLoadMorePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaSpelhusetTeaserTest.verifyOnePlusOneTeaserGroup","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveAllChannelsInMainView","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTitleRights","FAIL","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveMainContainerWithArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveArchivedPostsInRightColumn","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.VTmaTest.allTests","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHomeLinkThatNavigatesBackToFrontPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveLinksToComments","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveIconImageInBlockHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectNumberOfBlockNavigationElements","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldNotBeAbleToPostACommentIfUsernameIsNotEntered","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldNotHaveTextPresentUnderMostOfTheTitles","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.footerShouldExist","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theViewAktuellaShouldIncludeActiveLinksAndNoInactiveLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.FooterTest.shouldHaveSVTPresent","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldEmptySearchFieldWhenSearchFieldIsActiveAndUserHitsEscape","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.SVTWorldTest.shouldHaveMenuWithSevenTabs","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldBePresentAndDisplayedWithInfoAndImage","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveChef","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompahusetTeaserTest.verifyOnePlusTwoTeaserGroup","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.SvtWorldTest.shouldUpdateTvTableAccordingToSelectedTimeOffset","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoPlayerTest.shouldBePossibleOnTitlePage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Svt2SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Kanal9SingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnStatistics","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv8SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveGlobalFooter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnFootIncludes","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldHaveMoreButton","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.TablaTest.shouldShowScheduleOfToday","FAIL","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHaveGreyBackground","SUCCESS","firefox","any"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowVotingOptionsWhenActive","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedList","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.headerShouldExist","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderLinkToCraftsPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldExpandArticle","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldExistInXSmallDimension","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.shouldHaveLinksInFooter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldVerifyThatTeaserHasEpisodeTitle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveRelatedObjects","FAIL","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldExpandXSmallMoreMenu","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldHavePlayButton","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveCharacterImagesPresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveAllRelatedSitesSection","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveSwipeTeasers","FAIL","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHaveTextContent","FAIL","firefox","any"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldGetHistoryListWhenWatchingVideo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.PSLBlogPageTest.shouldHaveSvtHeader","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.searchTermShouldBeHighlighted","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldShowSearchFieldWhenClickingOnSearchIcon","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldHaveInputFieldInHeaderInDimensionLargeAndMedium","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveTags","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.HistoryListTest.shouldGetHistoryListWhenWatchingVideo","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldSetFocusOnFirstSearchHitWhenSearchFieldIsActiveAndUserHitsEnter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatFutureBroadcastHasButtonsForPrevEpisodes","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldHaveMoreThanOneVotingOption","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveRightColumnWithArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveGridWithCurrentTitles","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.ScribbleLiveHeaderTest.templateShouldHaveProperContainers","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveVideoList","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldHaveBasicInformationIfRegular","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.SiteNavigationTest.shouldHaveHeaderWithNavigationElements","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Svt1SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.PostTest.shouldHaveContent","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.SjuanSingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.WordPressTest.shouldHaveTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeInstagram","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FrameworkTest.shouldHaveFrameworkBlocksOnABlockPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveSeProgramPageWithPlayerPresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatMainAreaContainsArticleList","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldHaveLinks","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldVerifyThatSideAreaContainsArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldBeDisplayed","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveCharacteristics","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeTwitter","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveBlockNavigation","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveEpisodeMeta","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldHaveFiveListEntries","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoInformationTest.shouldHaveGeoBlockContent","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedVideoThatCanPlayVideo","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInSmall","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHavePosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveMoreButtonOnSmallScreens","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldBePresentOnAllTypesOfPages","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.BarnkanalenSingleChannelTest.shouldNavigateToDifferentSingleViews","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveCorrectItemCenteredInList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.FooterTest.shouldReturnFooterContainingOnlyOneLogo","SUCCESS","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHaveQuotesAsImage","FAIL","firefox","any"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderLinkToGamesPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldStayOnPageIfNotFoundAndHitReturn","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.nonsenseSearchShouldReturnEmptyResult","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnStatistics","FAIL","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldHaveKunskapskanalenTitleSuffix","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveShareAndPrintInfo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveVideoList","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.viewingListingFromYesterdayShouldHideAsideListings","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldHaveExpandableVotingOptionsIfInactive","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnFindAnswersPage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.aoShouldHaveNavigationList","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FourOFourPageBarnTest.shouldHaveFooter","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatAHitHasADateAndAnIconForChannel","FAIL","firefox","linux"}
		,{"se.svt.test.nss.AnalysisTest.shouldHaveAnalysisVignette","SUCCESS","firefox","any"}
		,{"se.svt.test.barn.barnkanalen.FastFlowTest.shouldHaveVideoPosts","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.escenic.FlowSectionHandler.createDateSectionsForFlowPlaylist","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewResponsivenessTest.shouldDisplayTwoCompleteChannelsInSmall","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FrameworkTest.shouldHaveFrameworkBlocksOnATitlePage","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldLinkToEpisodeModal","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldShowTimeToPrepare","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveVideoPlayerThatCanPlayVideo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldHaveLoadMoreButton","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifySectionBasedPlayerContainsEightThumbnailsAndAnAnchorId","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.EpisodeModalTest.shouldBeDisplayedWhenClickingOnEpisodeInListUnderCharacterList","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaTitlePageTest.shouldHaveMenuLinkToAllRelatedSites","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveSwipe","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.newsArrowsShouldPointToNews","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleCommentTest.shouldBeAbleToPostACommentWhenModeratingIsOn","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.viewingListingFromTodayShouldDisplayAsideListings","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyLocalHeaderMinHeight","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldHaveLogo","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldExistInRightColumnOnPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldChangeActiveTitleInSearchResultListWithTabKey","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldGivePossibilityToVote","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldHaveEntriesWithLinkAndDateAndTypeInfo","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldBelongToAProgram","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsVideoPlayerTest.clickVideoThumbnailAndVerifyThatCorrectVideoIsStartedInClipPlayer","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.tablaPageShouldContainSearchBox","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsBlockTest.shouldHaveCorrectNumberOfBlocksOnTestedPage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationShouldWork","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldHaveBasicInformationIfRegular","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationWithoutJavascriptShouldWork","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldHaveTitleListsDisplayed","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.FeaturedEpisodeTest.shouldShow404PageWhenExpiredEpisode","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveOpinionTeaser","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.verifyAreaBasedPlayer","FAIL","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnDocTypeData","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveMenuAndNavigationBar","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldBePresent","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveShareAndPrintInfo","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldToggleSearchFieldToSearchIconInSmallWindowMode","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.FeaturedTopTest.shouldVerifyThatFeaturedTopArticlesContainsImages","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.ContentListTest.shouldExpandArticle","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.MoreOnSvtAreaTest.moreOnSvtAreaShouldWorkAccordingToSpec","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticleRightColumnTest.shouldHaveRelatedArticles","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveMoreButton","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnAppIcons","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeSticky","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.VTmaTest.allTestsOnTvTabla","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.tabla.TablaSearchTest.paginationButtonsShouldExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveCharacteristics","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FlowAdminTest.shouldLoadMorePosts","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeSticky","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.frontpage.NewsAreaTest.smallImagesShouldBeAlignedToTheLeft","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldBeDisplayed","FAIL","firefox","linux"}
		,{"se.svt.test.atv.MelodifestivalenTest.shouldHaveCustomizedPageTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SearchTest.shouldStayOnPageIfNoInputAndHitReturn","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.barnkanalen.FirstPageFrameworkTest.shouldHaveFastFlow","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldGivePossibilityToVote","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SmokeTest.testSomePagesUnderBarnkanalen","FAIL","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveMainContainerWithArticlesOnNewsPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveRecentPostsInRightColumn","FAIL","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldHaveCookingInfo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.external.TemplateResolverTest.shouldReturnFootIncludes","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.VotingTest.shouldShowImageForVotingOptions","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveLocalMenu","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveLinksToTitlePagesOnBarnkanalen","FAIL","firefox","linux"}
		,{"se.svt.test.atv.KunskapskanalenTest.shouldHaveSchedulePageShowingScheduleForAtLeastTodayAndTomorrow","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theViewAllaShouldIncludeBothActiveAndInactiveLinks","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.FeaturedTopTest.shouldVerifyThatFeaturedTopArticlesContainsImages","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.TopListTest.shouldHaveTwoTabs","FAIL","firefox","linux"}
		,{"se.svt.test.atv.widgets.FooterTest.shouldReturnFooterContainingOnlyOneLogo","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LocalHeaderTest.shouldVerifyThatHeaderElementsExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompahusetTeaserTest.verifyTwoPlusOneTeaserGroup","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHavePlayerWithCorrectAttributes","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.KidsVideoPlayerTest.verifyClipPlayerExistence","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theViewAktuellaShouldIncludeActiveLinksAndNoInactiveLinks","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveTVGuide","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.HeaderTest.shouldHaveHeaderLinkToGamesPage","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.MVHBlogPageTest.shouldHaveLocalMenu","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatAHitHasATitleAndADescription","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.InnehallAoTest.clickingOnAnchorLinksShouldTakeUsToTheAnchorTarget","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.CharacterListTest.shouldHaveLinksToTitlesInBarnplay","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaSpelhusetTeaserTest.verifyOnePlusOneTeaserGroup","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldShowTitleListWhenSearchingOnTitles","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeYoutube","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveRelatedContent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.FutureBroadcastsTest.shouldVerifyThatAHitHasATitleAndADescription","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveEpisodeMeta","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldShowNumberOfPortions","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.FrameworkTest.shouldHaveFrameworkBlocksOnABlockPage","SUCCESS","firefox","linux"}
		,{"se.svt.test.svtse.programao.ProgramFilterCategoriesTest.theCategoryBarnUrlShouldNotContainSportTitles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.programpages.NextEpisodeTeaserTest.shouldLeadToCorrectPageAfterClickOnVideoSectionLink","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.BredlageTest.shouldHaveWideviewTitle","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.BolibompaSpelhusetTeaserTest.verifyOneTeaserGroup","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.LatestEpisodeTeaserTest.shouldBeClickableAndStartVideo","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldHaveIngredients","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveEpisodeTitle","FAIL","firefox","linux"}
		,{"se.svt.test.atv.global.Http404Test.shouldHave404WithMenuAndFooter","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.FooterTest.footerShouldExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TablaTest.shouldShowScheduleOfToday","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveSeProgramPageWithPlayerPresent","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.AlphabeticalIndexPageTest.shouldExistForAllPrograms","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.TittarserviceTest.shouldHaveCorrectComponentsOnAskUsPage","FAIL","firefox","linux"}
		,{"se.svt.test.barn.play.CategoryListTest.shouldDisplayCategoryIcons","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldExist","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.MediaGalleryTest.shouldOpenArticleAndChangeImageWhenClickingOnLargeImage","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.MainViewTest.shouldHaveTablaForThreeWeeks","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.scribblelive.LiveArticlePostTest.shouldBeOfTypeFlickr","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.NewRecipeArticleTest.shouldBePresent","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.widgets.PollTest.shouldShowVotingOptionsWhenActive","FAIL","firefox","linux"}
		,{"se.svt.test.atv.programpages.VideoPageTest.shouldHaveEpisodeTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.TitlePageTest.shouldHaveVideoClipPlayer","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveRecentPostsInRightColumn","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.SmokeTest.testSomePagesUnderBarnkanalen","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.CharacterListTest.shouldHaveTextPresentUnderCenteredTitle","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.escenic.FooterTest.shouldHaveLinks","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.tabla.singlechanneltest.Tv4SingleChannelTest.shouldNavigateToDifferentSingleViews","FAIL","firefox","linux"}
		,{"se.svt.test.atv.umbrella.UmbrellaSectionPageTest.shouldHaveRightColumnWithArticles","FAIL","firefox","linux"}
		,{"se.svt.test.barn.escenic.VideoDeepLinkTest.videoPlayerWithOnlyOneVideoShouldShowNoVideoList","FAIL","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveRssSubscribeButton","FAIL","firefox","linux"}
		,{"se.svt.test.atv.RadiohjalpenTest.shouldHaveFooterWithLogos","SUCCESS","firefox","linux"}
		,{"se.svt.test.barn.play.SearchTest.shouldGiveErrorMessageWhenSearchingOnNonExistingTitles","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.blogs.EscenicBlogMainPageTest.shouldHaveSvtHeader","SUCCESS","firefox","linux"}
		,{"se.svt.test.atv.recipe.RecipeArticleTest.shouldBePresent","FAIL","firefox","linux"}
		,{"se.svt.test.svtse.GlobalSearchTest.searchUsingTheHeaderSearchFieldShouldGenerateHits","SUCCESS","firefox","linux"}
	};
	
	public static Map<String, String> getRandomTest(){
		Map<String, String> data = new HashMap<String, String>();
		int row = (new Random()).nextInt(DATA.length);
		String testName = DATA[row][0];
		String status = DATA[row][1];
		String browserName = DATA[row][2];
		String platformName = DATA[row][3];
		
		data.put("name",testName);
		data.put("status",status);
		data.put("browser",browserName);
		data.put("platform",platformName);
		data.put("version","prod");
		
		return data;
	}
    private UUID uuid = UUID.randomUUID();

    public void beforeTest(Map<String, String> data) {
        String msg = toJson("1",uuid.toString(), data, 0);
        sendByUDP(msg);
    }

    public void afterTest(Map<String, String> data) {
    	int delay = (new Random()).nextInt(1000);
        String msg = toJson("2",uuid.toString(), data, delay);
        sendByUDP(msg);
    }

    public String toJson(String type, String uuid, Map<String, String> data, int delay) {
        long timeStamp = DateTime.now().getMillis() + delay;
        String browser = System.getProperty("browser","any");
        String version = "prod";
        String platform = System.getProperty("platform","any");
        String size = System.getProperty("size","any");
        String host = "http://stage.svt.se";
        String grid = "http://svt-stoprod-seleniumgrid01:4444/wd/hub";
        String user = "jenkins";

        Map<String, String> env = System.getenv();
        int row = (new Random()).nextInt(DATA_BUILD.length);
        String buildNumber = DATA_BUILD[row][0];//"6760";
        String buildId = DATA_BUILD[row][1];//"2015-08-09_16-58-13";
        String buildUrl = DATA_BUILD[row][2];//"https://barnjenkins.svti.svt.se​/job/escenic-barnkanalen--regressiontest/6760/";
        String jenkinsUrl = DATA_BUILD[row][3];//"https://barnjenkins.svti.svt.se​/";
        String nodeName = DATA_BUILD[row][4];//"master";
        String buildTag = DATA_BUILD[row][5];//"jenkins-escenic-barnkanalen--regressiontest-6760";
        String jobName = DATA_BUILD[row][6];//"barnplay-regressiontest";

        
        String gitCommit = "2b24b4b3db76d5d9b7d00d272502ab7d526b1c25";
        String gitURL = "git@bitbucket.org:svtidevelopers/svtse-automated-browsertests.git";
        String gitBranch = "origin/master";

        Map<String,String> status = new HashMap<String, String>();
        status.put("type", type);
        status.put("uuid", uuid);
        status.put("timeStamp","" + timeStamp);
        status.put("platform",platform);
        status.put("size",size);
        status.put("host",host);
        status.put("grid",grid);
        status.put("user",user);
        status.put("buildId",buildId);
        status.put("buildNumber",buildNumber);
        status.put("jobName",jobName);
        status.put("buildUrl",buildUrl);
        status.put("jenkinsUrl",jenkinsUrl);
        status.put("nodeName",nodeName);
        status.put("buildTag",buildTag);
        status.put("jobName",jobName);
        status.put("gitCommit",gitCommit);
        status.put("gitBranch",gitBranch);
        status.put("gitURL",gitURL);
        status.putAll(data);
        
        if("1".equalsIgnoreCase(type)){
        	status.put("status","STARTED");
        }
        
        String result = status.get("status");
        if("2".equalsIgnoreCase(type)&&("fail").equalsIgnoreCase(result)){
        	if((new Random()).nextInt(100)>10){
	        	status.put("status","SUCCESS");
	        }
        }
        String json = new Gson().toJson(status);
        return json;
    }

    protected String statServer = udpHost;
    protected int statServerPort = udpPort;

    protected void sendByUDP(String msg) {
        try {
            DatagramSocket sock = new DatagramSocket();
            InetAddress addr = InetAddress.getByName(statServer);
            byte[] message = (msg + "\n").getBytes();
            if(do_log_sent_testdata) {
                log.info("sending: " + new String(message));
            }
            DatagramPacket packet = new DatagramPacket(message, message.length, addr, statServerPort);
            sock.send(packet);
            sock.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
