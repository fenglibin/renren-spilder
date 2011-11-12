package it.renren.spilder.util;

import it.renren.spilder.util.log.Log4j;

/**
 * ¼òÌå·±Ìå×ª»»¹¤¾ßÀà
 * 
 * @author Administrator
 */
public class FontUtil {

    private static Log4j        log4j  = new Log4j(FontUtil.class.getName());
    private static final String fanti  = "°¥°§°¦°£°¤æXÞß°}°©‡†°«Ì@ì\°¬ÛíÁ°¯àÉ‹ÜµK•á­aƒvËB°²°¸°±âÖÖOÄWùg°°°²±V†±ˆ°³°³ˆä@Þîáí°¶°´°¸°·°µ÷ö°¹°º–‹°»ál°¼Ûêˆ n°½à»åâÚéáåÛ°¾­H°¿ñúòüÖ’ü÷éÆb°Ã‹‹ŽS°ÁŠSŠWòˆ°Ä°ÃöË°Ë°Í°È°Ç°Ç°ÉŠBá±°Å°Ì°Æ°ÊôÎØ^÷„°ÎÜØÃ_°Î°Ï÷É°Ñ°Ð‰Îy°ÖÁTõE°Ôå±êþ°×°Ù°Û°Ø–àÞã”[†h”¡°Ý°Þ°â”‘°à°ãîC°ß°á”Ê°ã°ßÚæÚæ°å°æâkô²°æô‘Þk°ë°é°çŠ”°è°è°ê°îŽÍ°ðäº°î°ñ°ò°ö°ø°ôÖr‰Y¶œÝò°õæ^Ùè°üæß°ú°ûìÒý_°ý±¢Œšï–±£ød±¤ÝáñÙˆó±§±ªõÀ°ûõU±©Ì™±¬Úé±°±­±¯±®ùl±±ØªNÚý‚ä±³ä^±¶ã£±»‚³‘v±ºÝ…íÕÝíñØóc¼LíxöÍ±¼ÙS‚–åQ±¾±½ÛÎÛÐ±¿È±À¿‡±Ä±ÂÈE¬e±Ã±Åê´±À±ÄŒÂ±Æös–ÄÝ©±ÇØ°±ÈÓßÁåþ›a±ËïõÙÂ¹Pô°±É±Î±Ø®…é]±ÓÔvß›î¯†ô±Ñ«Éœ±Ý”ÀáùãGæ¾âØ±Ö—a±°ˆãåöÏã¹°zº`Ø§Ÿ•±Ô±Æ±ÍÆ¢Û‹és±×±Ìóë±ÎñEñƒó±ÚæÔó÷ÞµÓv±ÜõIå¨±Û÷ÂŠ`èµð{ôÅÒgÜLú‡ß…í¾ß…¾ŽìÔ¹òùöc±ÞÙH±âñ¹ØÒÆ±â·HñÛËx±åÛÍâí’\›MãêÜÐ±ã×ƒ•c¾Ž¾œ±éÅŒ±æÞqÞq±ë˜ËïRªYŽ¼±ëòŠŸÏ±ìñ¦çSïjïjždès±íæ»ñÑÒF™~‚l÷B±ïü‚„eõ¿°Tß“Ùe±òƒ†±óÙeÀ_™‰è\žlÓÄÏ™”Pš›ÄœóxôW±ù±ø–Þ±ûÚû±ü±ú±þïžÍs·AKŽÕ²¡Þð“Ü²¨²£„ƒÒUÀðGà£²±²¤ó²¥²®ØÃñg²¯²´­“²ªÙñ›Ââ“ãK²°²©²³È•ùP²«ðoõNƒk²­²²õÛæn±¡ÒqíçõËô¤ë¢éÞ²¶ŽïÆÒâ˜êÎðJõ³ÊNÑaÑa²¸²¶²»²¼²½²Àâb²¿²ºê³²¿º^²¾·Ôº»²¡ÆÙàê²Áíå²Â²Å²Ä²Ä²Ã²É²Ê²Ç¾Z²È²Ë²Ì¿n‘Kò‰²ÍšˆÐQ‘M‘K‘”üoüo NôÓè²‚}‚áœæÉnÅ“²Øè†²Ù²Ú²ÜàÐäî²Ûô½ó©²ÝƒÔ‚ÈŽúœyœy”˜²ß¹‹á¯—qä¹àáŒÓ²ä²æè¾Åa²è²åðlåšÅ‘ˆ“²é²ç²è¿²ëâª²î²ì²êéßñÃ²ìãâ²í÷ÔŒæ±²î²ðâOƒŠ²ñ²òÏŠðûÒ—“½”vŽÊ‹È×‹åî¶Uð’ÀpÏsäaâÜäýç†Àpó¸Žfõðð’®b„}®aÕ~çPêUÊr‡ÏÀA‘Ôîåñí]‚t²ýæ½²þÝÅé‹¬döKÄcÈO‡Lƒ”³£áäæÏ÷lSˆöêÆã®³¨ë©ä¬„•³³«ÛË³ª•³ío³­€â÷ân¾b³¬êË³²³¯³°³±³³³´±|ûžñéÜ‡³Œ†q³¶³·Ûå³¸³·³ºÞÓ³»è¡àÁ‰m³¼³À³Á³½ê³½³¿ÖRÚ’´~‰}Û{êJÒr¯M·QýZ³Ã™Â×™f‚ Ï|èK ª¬bÚX“ÎîªØ©³É³Ê³Ð—–Õ\àJ³ÇŒk³ËÛôÃ”ä…‘Í³ÌñÎëóõ¨³Î³È³ÑòG³Ó³Ô³àò¿ø|®Eí÷ó×àÍæÊ“¤°Vó¤÷Î³Ú³ØñYßtÜÝ³ÖÜ¯õØóø³ß³ÞýXuôùñÝáÜß³³â³àï†’xŸë³áë·à´ÙÑ‘yñ¡³ä›_âç³äÁˆ´º‘oã¿ô¾Ïx³çŒ™ã|³éñ¬ºN ß³ðƒ‰® ã°¾I® ³î³í»I³êÜP‘À×‡áh³ò³ôšŽ³ö³õ“¹éËÆc³ý™»³üäzòÜër™»ŽÐ³ùÜXèÆµAƒ¦èú³þñÒýƒØ¡ÌŽâðµA¬`´¤Ó|âðšb÷í´£ÞõÄu´§à¨àÜõß´¨´¨ë°´©‚÷ô­´¬´§´ªšNâ¶ÇF´­ƒb´®âA´°¯´°“œ´²‡l„“í´µ´¶´¹Úï´·Ç”é¢é³åN´º‰@´»òíöj¼ƒ´½É”´¾ù‡_´¼Ûw´ÀõÖ´ÁÞu´Ù¾bßOÝzýpšfßÚ«u´ÃÔ~ìôÜë´Ä´É´ÈÞo´Å´Æú\ôÙ´ËÕ´Î´Î´ÌÙnÄ´ÒÄ¿vÊ[ò‹è®Â”…²äÈ×Øœ×àëíÝ´ÖáÞéã´Ùâ§×å´×´Øõ¾õíÙà”xè‰Üf¸Z´Ûìà´Þ´ß´ÝéÁè­´àßýã²´ãÝÍÇË´á´â´ä´åñå´æââ´ç´ê´è´éõãáÏðîïóûzëâÎô´ì´ëäSåeÉ²ÐóÔøëúå¤ò–ÙYÒbÑnº@´¼‡}ÞÇ´îËþñ×ß_æ§âòóÎ´ð´ñ÷°í^´ò´ó´ô´õ´ö´úá·ß°½HåÊŽ§´ýµ¡´ùçéÙJÜ¤´ü´þ´÷÷ìµ¤†Î“úíñµ¢àñõš—°Dº„ÙÙÄ‘ðã¼“Ûµ©µ«ÕQà¢—‘„µ­ÝÌµ°µªµ­®”Òd“õüh×•šëå´´XÊŽ™nÝÐµ¶µ¶âáë®Œ§uµ¹“v¶\µ¸µ½µ¿±IµÀµ¾ôîµÃåuµÂµÄŸôµÇàâµÅµÅµÈ´Áà‡µÊáØµÉíãç‹µÍôÆµÌµÎµÎçCíLµÒ¼eµÏ”³œìÝ¶µÑÓ]µÕØµÔgÛ¡Ûæµ×µÖèÜíÆ÷¾µØµÜ«ZµÛæ·ßfµÚÖBé¦íû¾†µÙíÚµùµàµáîŽp°dµäücµâõÚëŠµèµéÚçÛãµê‰|çèâšµëÕµìµîñ°ô¡µóµðµñõõµïµñõ µõážµôµõµùµøµüÛìµøÕ™Ü¦ÞéñóµüëºµúµûõÞöl¶¡Øê¶£¶¡ðÛ¶¢á”ñôôúí”¶¦Ó†¶¨à¤ëëíÖåVGäA–|¶¬ßËƒöù…¶­¶®Ê„Óƒö¶±Ûíá¼¶²—¶´ÄLëØíÏ¶¼¶µÝúóûôY¶¶¶¸ò½¶¹àK¶º¶»¸]¶¼¶½¶¾×x Ù™³ © Ùüt÷ÇªšºV¶ÂÙ€¶ÃÜ¶¶Ê¶Å¶Ç¶È¶Éåƒó¼¶Ë¶Ì¶Î”à¾„é²Ÿå‘»f¶Ñê Œ¦ƒ¶µqŒ¦¶Ñí­ç…‡ª¶Ø¶Õíâ¶×íïÜO‡Ÿõ¶Üí»âgîD¶Ý¶àßÍ¶ßñÖŠZèI¶Þ”£õâ¶äßá¶â¾E¶ã¶çãõ‰™¶æ¶è¶åÙyã“á”äb³j¿ÉåíÓž¶í¶ð¶ëÎÒä~ùZ¶êî~°¢¶òêi°¦¶ó¶òÜ—ˆ×ºðIÖ@¶õé‘ãµÝà¶ôý|åŠù˜î€Ø¬Ö@÷{¶÷ÝìÞôƒº¶øøõb –¶úßƒ¶ýðDçíãs¶þÙ¦ÙEßíÕOü–µp‚¿°l·¦·¥ÛÒÁPéy·¤·¨íÀ·¨·«·¬á¦·­·ª·²µ\âCŸ©·®Þ¬ìÜ­[·±õì·±ÞÀçx·´·µ·¸·¸·«·ºïˆ¹ Øœî²èó·½Úú·»·¼èÊâ[·À·Á·¿·¾ô™·ÂÔL¼•Pô³·Åïwåú·Ç·ÈŠó¾p·Æìéòãö­öE·ÊäÇ·Î·ËÕu·Çì³é¼ôäóõ·ÍU·Ðáô·ÎÙMðòçš·Ö·Ô¼Š·Ò·Õ·Ó‰ž·Ú·Ò·Ù÷÷·Û·ÝŠ^·Þƒf‘¼S÷aå¯ØSïLž–œt—÷·â¯‚´^·å·éÝ×äh·äÛºñT·ê¿pÖS·îøP·îÙºó¾·ñ·òß»ÄwõÃûŸïûõÆ·õ·ó¸¥·üøDæÚ·öÜ½ÜÀâö·÷·þ¼›½E·û·ý–¢·úìðî··üÛ®¸¡íÉÝ³ò¶Ùëèõ¸¢¬M·ûåõÝÊ¸¤·ù¸£Ý—ÒLòðíê“á¸¦¸®ÞÔ¸«¸©¸ªÝo¸­äæ¸¯íë¸¸Ó‡¸¶‹DØ“¸½¸À¸·ñ€Ñ}¸°¸±¸µ¸»Ùx¿`¸¹õVÙŽÎlòóöv¸²ð¥ÐñÙ¤áæÙ¸Â¸ÁæØÞÎÔ“º¢ÛòÙW¸ÄØ¤â}Éw¸Èê®¸ÅŽÖ¸ÊŽÖ¸Ë¸ÎÛáãï¸Ê¸Ì¸ÍðáôûŒÀ¶’Ús¸Ò¸Ðä÷éÏß¦êºí·½CäÆŽÖÚMŒù„‚¾V¸Ø¸×ä“î¸¸Û¸Üóà‘ß¸Þ¸á¸ßØº¸à¸Ý¸âê½¸ã¿céÂ¸å¸ãéÂ¸æÕaÛ¬ä†¸êÛÙ¼v¸í¸ç¸ìñËø¸î”R¸èéw¸ï¸ñØª¸ð¸ôàÃÜªë¡ëõæk÷ÀÙRô´‚€¸÷ò´íÑãt¹w¸ù¸úºÝƒ¸ù¸ú¸ü¸ý¸ûÙs¸þàQÓ²¹¡½Ž¹¢¹£õ†¹¤¹­¹«¹¦¹¥¹©ëÅŒm¹§ò¼¹ªýö¡ì–¹¯¹°¹°¹²Ø•¹´¾Ðã^âh¾—¾—óô÷¸á¸¹·¹¶èÛ«vóÑ˜‹ÔÙ¹¸‰ò˜‹ì°åÜÓM¹À¹¾¹Ã¹Â¹ÁÝMø¹½¹½òÁõý¹¼ôþÝž¹¿úX¹Åãé¹À·Y¹Éêô¹Çî¹¹ÁÐM¹ÄØÅ·Yëûî­¹Ì¹Êî™áÄèôêö¹ÍðóådöA¹Ï¹ÎëÒøŽ¹Ï¹Î¹ÑØÔÔŸ’ì¹Ó¹Ô¹Õ¹ÖêPÓ^¹Ù¹ÚÙÄ¹×öŠð^¹ÜØž‘T“¥¹ÜîÂ¹à­ûX¹Þ¹âßÛèæë×V«E¹äšw¹ç‹‚ý”ÒŽð§é|Îù¹åõqå³Ü‰âÑÜ‰ÔŽ¹ï¹íêÐóþ„£„¥™™êÁÙF¹ð¹ò÷ZÐ–¾i¹÷LLõ…¹÷†Jˆå¹ùáÆñøåÏX‡ë‡øŽ½“ë½Ùå¹ûâ£˜¡òä¹üß^™u¿©Ý¸Ñºðgì¸òù]¹þ¹þº£º¢º¡º£ëÜõ°º¥ñ”º¦º¤í™òÀº¨º©÷ýÚõº¬ºªº¯º¬êÏº­ìÊº®ínº±º°hº¹ºµº·º´º¸¬HÝÕ‚þîhÞþº¶º³º²å«º¼½Wº½î@ãìÝïàãÞ¶Ï–ºÁàÆºÀº¿º¾ºÀºÃºÂÌ–ê»ºÆºÄœBð©î—ž®ž®ÔXºÇºÈºÉºÌºÏºÎºËºÍºÓºÈéuºËîÁºÉºÔºÐºÊÔZîMêHôçÞÙRºÖºÕúQÛÖºÚºÙºÛºÜºÝºÞºàºßºãèìçñ™MºâºâÞZºåÙêºæÞ°ºë¼tºêébãüºéÈ‡ºçø™Þ®üZÓºîºíºïðúóóºí÷¿ºðááàCºñåËºòÜ©÷cºõºôºöìÃÜ ºöã±äïàñ»¡ºüºú‰Øõúºþâ©ºùºýº÷ù–éÎºýºûõ­ì²»¢G»£çú»¥‘ôÙü×oœûá²âïìæìïóËìèð­à‚ûI»¨ÈA‡Wò‘çf»¬»«»¯„®‹Ô’˜å‘Ñ»²»´»±õ×‰Äšgâµß€­hä¡»¸ÝÈæD­hÀQ÷ß¾»ÃŠJ»Â†¾“Qä½œo»¼Ÿ¨åÕ¯ˆ»¿»¼õŒß§ëÁ»Ä»Å»Ê»ËÚòüSáå»ÌäÒåØ»Íäêè«óò»Èñ¥»Ç»Éó¨öm»Ð»Î•sÖe»Ïœê»ÒÔœ‡j»Ö“]ò³•Ÿ¬qÝx÷â»ÕãÄ»Øä§Üî»×»Ú»Ü…R•þÖM‡‚•þÀLËCÕdí£ ZÙVåç»Þ·xà¹»ÝÀDš§»ÛÞ¥ó³»èÈ»éé’œ†ðQ»êÕŸ»ì»ìñëåxØå»íß«»î»ðâ·â·â·»òØ›«@µœ»ó»ô«@ÖožmÞ½ó¶ÐÐí¹È¦°n¹k¼Õ‘Ø¢×I“ô‡\ð‡ØÀ»ø™C­^¼¡Ü¸´‰ëu¼ªÛEØÞßó¼§åì·eóÇ»ù¿ƒïúê÷¾ƒýV»ûÜQ»þçÜ»üýWÛÔ¼¤Áb¼°¼ªá§¼³¼‰¼´˜OØ½Ù¥Š ¼±óÅ¼²ê«¼¬éê¼¯¼µé®ÝðÝ‹ñ¤Þª¼®Ž×¼ºÏl”D¼¹ÒÎêª¼¹÷äÓ‹Ó›¼¿¼o¼Ë¼É¼¼ÜÁëH„©¼¾”D¼ÈÔÛúÀ^ÓJÙÊ¼Å¼Ä¼Â¼ÀËEôßõÕìV÷qð¢öa¼½÷ÙóK¼ÓŠA¼ÑåÈ¼Ï›Ñçì¼ÒðèóÕôÂÏ¼õÊ¼Îæ‰áµàPÇví¢ê©äeÍîa¼×ëÎÙZâ›ðýƒrñ{¼Ü¼Ù¼Þ¼Ú‘â¼é¼âŽÔˆÔšžég¼çÆD¼æ±O¹{¹×Ç°êù¾}Þö¼å¿VÝóöžúYº]žhídàî’þ—gƒ€¼íÀO™z¹aœp¼ô™zõÂ²€û|ÒMç™º†×v‘ì‰AôååÀå¿ÒŠ¼þ½¨ðT„¦êðË]Ùv½¡¾ÅžuÖGæIë¦ÙvëìÛ`èbæIÙÔ¼ý½¨½­½ªŒ¢½­{ôø½©í\íä½®Övª„{ÊYñð½³½µä®½dáuŠôÝÜ´½»½¼æ¯òœ²½»òœÄz½·½¹òÔõÓÙÕõo½¶½¸ú„ç€½ÇÙ®“×½Æ½gïœð¨³CÄ_ãq”‡½Ëë¸ƒeÀU½ÐÞIÝ^½Ì½Ñ½ÍàÝõ´ëA°X½Ô½Ó½ÕÖCàµ½Ò½ÖæÝ¹Ó“½Ù‚ÜÔ‘Þ×½Yèîæ¼½ÝîR½Þ½ØíÙ½ßõ^ôÉ½ã½â½é½ä½æŒÃ½ç½êÕ]½èò»÷º½å½í½ñ½ï½ð½òñæñÆßM½î½óƒHŽ„¾oÝÀÖ”å\âÛð~éÈèª±M„Å½ñ½üßMË|•x½þ aÚB¿N½û½ùÓP½ó¾©›Ü½›ÇoÇGó@ìºÝ¼¾§ëæ¾¦¾¬¾¤¾«öL¾®Úå„qëÂîi¾°ÙÓ¾°¾¯œQ†½ÞŸÃ„¯d¸‚æº¾¹¾´¾¸¾³¾¹ìoçRìçåÄ¾¼¾½¼m¾¿øFôñôb¾¾¾¾÷Ý¾Å¾Ã¾Ä¾Á¾Â¾ÆÅf¾Ê¾Ì¾ÎèÑèêŽý¾È¾Í¾Ë¾Íú¾Ó¾Ð¾ÑÇÒñx¾ÒÞäé§“þä|ñÕöÂ¾Ï÷¶¾Ö½Û¾ÕéÙ¾×¾ÚÅe¾ØÜì™Îé°ýeõá¾ä¾ÞÔn¾Þ¾ÜÜÄ¾ß¾æîÒ¾ãÙÆ¾Ó‘Ö“þ¾àêøïZä¸M¾ÛŒÕ¾áåáõ¶¾ê¾è¾èùNçîÃ¾íäŸ¾ë—¨áú½ëh¾ìÛ²¾ï¾ïæÞ¾ñÔE¾ñ«i«k½^ÓX¾ó¾ò¾òèöõûØÊØã×Hâ±Þ§éÓ¾ôè‘õê½ÀÛÇìß¾ðÜŠ¾ý¾ùâx°—¾úóÞ÷å¿¡¿¤¾þÞÜ¿£òE¬B¿¢’¶òÂ™‘†U¿§¿¦¿¨ØûëÌé_¿«ãl„P„P‰Nðæz¿®ÝÜ¿¬å|÷¿¯¿±ý¿°ê¬¿²Ù©¿³Ý¨¿´êRî«¿µ¿¶¿·÷K¿¸¿ºØøß’¿º¿¹é`¿»â‚åê¿¼¿½¿¼¿¾äDêû¿¿¿À¿Á¿Âçæ¿ÆÝVåíâŽ¿ÃîWïýñ½îwî§¿Äòò÷Áš¤¿È¿É¿Á¿Ê¿Ë¿Ì¿Íã¡Õnë´òS¾~¿È¿Ää˜¿Ï‰¨‘©¿ÐñÌ¿Ô¿ÓçH¿ÕÙÅáÇóí¿×¿Ö¿Ø“¸¿×²g¿Úßµ¿Û¿ÜóØÞ¢ØÚ¿Ý¿ÞÜ¥¿ß÷¼¿àŽì½f‡¿Ñ¿áÕFÙ¨¿å¿æ¿è¿çØá‰K¿ìƒ~à”‡ˆªœÄ’¿êŒ’óy—p¿î¿ïÕEßÑ¿ð¿ñÕNÞÅà—‰¿µV›r•çµVÙL¿ò¿ôÌŽhã¦¿ø¸Q¿üåÓØ¸¿üÞñ¿ûêÒ¿ýî¥òñÙç¿þõÍ…Tà°‘|À¢ðÊ‰ð™™ºˆÂ˜À¤À¥ŸjçûåK÷Õõ«öHã§À¦é€‰Ú—yÀ§”UÀ¨èéòÒéŸÀªà~ìn“š˜ÍÀ¬À­À²Áååê¹íÇÀ®´ÌÏžðøÏžÀ±íˆÆœZÈRånÙl²AÙ‡ž|°]»[Ìm¹”r™ÚÀ·ê@Ë{×Žž‘Òh”Ì»@è|Ó[”ˆÀ|™ìžEî½‘Ð €žEà¥ÀÉÀÇÝ¹ÀÈÀÅÀÆïüäZòëÀÊéÀËÝõ“Æ„ÚÀÎ‡Z“Æ°Aç„õ²ÀÏÀÐÀÑèáã™ÁÊ³ÀÓÂgÀÒ‹ªØì˜·Àß«WãîÀÕö˜À×æÐ¿wÀ×èDÙúñçÕC‰¾‚ñÀÚÀÙÀÜÀßœIîÀÛõªÀÞÀÕÜ¨ÀâÀãÀäÀãÀåÀæÀêëxÀòóPÀçà¬ ÀûZÀì¿rëxòÛæËÁ§÷~Àè»hî¾Þ¼÷óó»¶YÀîÑYÙµÁ¨æ²ßŠÀíä‡õŽå¢õ·÷kÁ¦•Ñ…–Á¢ÀôûÀû„î•Ñ‰ÈžrËžÀýìå™À…–ë`Àþƒ«™µóœÀóÞ]áBÀõáû«†µZµ[Î»à¦óÒÁ£¼cÏ ÀüÁ¡îºÜSìZäàóöƒÉŠYßBºŸ‘zßBÉÂ“ÑžÁ®ö–å¥ì¡ç ó¹”¿­IÄ˜ÒcÌ`¾šŒDŸ’‘Ùššæœé¬”¿Á¼›öÁº›öé£Á¿Á»Ü®õÔƒÉôuÁÁÕÝvÁÀÁ¿ß|¯ŸÁÄÁÅÁÈÁÎàÚå¼ÁÃÁÅ¿ÁÇç‚úá‘Þ¤ÁËÞÍÁÏÁÌßÖÁÐÁÓÙýä£ÛøÁÒÞæ«CÁÑôóõñ÷ààÁÖÅRÁÕÁÜÁÕôÔá×åàÞOÁØî¬Á×çl÷[ò•÷ë„C[‘¬™_ÁßÙUÌAì¢ÜkÁàÁæì`àòŽXÁîÜßÁàÁáê²œRâÁê™ôœR¬O¾cÁçôáñöÁâòÈÁãýgöNÛ¹îIÁîÁíßÊÁïÁï„¢žgÁ÷ÁôÁðÁòì¼åÞðsòtÁñÁöæyöÌÁø¾^äÁùúwýˆ‡µ‰Å‡µ™É­‡–Vµa»\Ã@Â¡ñªÁþë]‰Å‰Å”nŠäƒE‡DÊV˜ÇÂeÏNót“§“§ºtÂªÂ©¯›çUÂ¶ô””]±RÌJÌJ‰Àžo t™¾ÅFÞ_ûRÆAïB÷|­ˆûuÌ”“ïô”™©èu®fÁùÂÉˆvä›ÙTÝ`œOåÖÂ¹¬fµ“ÂµÂ·äõÂ¾ÞAÂºè´óüú˜Â´çešÚóHé‚…Î‚HÂÃ¿|ÂÄÂÉ‘]ÂÊ¾GŒ\Žn”™èû[ÅLž´èŽÂÑyÂÓÂÔäs’àö‚’àœS¾]Ý†Õ“ÞÛß‰«MÄTÌ}ß‰™åèŒ»jò…æ ÂÝÙÀÂãñ§Ùù˜·ÂåÀÒ Îñ˜çóÂäÞûäðöÃÆƒºÑ‹Œ‹ßÂéó¡ñR¬”¬”´aÎ›˜qµlÁR‡O‚ØôK†áÂïÂñö²ÙIÙI„êß~ûœÙuÃ}î”ÐUðz˜Ñ²m÷´ö MÏ\ÂüÖ™Ü¬á£ÂýÂþ¿zÂûì×çNÚøÃ¦Ã¢Ã¤Ã£íËÃ§Ã§òþØˆÃ«Ã¬ ÓÃ©ì¸òÖå^÷Öòúó±Ã®á¹ã÷Ã®êÄãTÃ¯Ã°ÙQë£ÙóÃ±à|è£î¦Ã²í®üN›]Ã¶ÃµÃ¼Ý®Ã·Ã½áÒäØâ­é¹ÃºÃ¸äYúBüqÃ¿ÃÀä¼œ„æVÃÃÃÁÃÄÃÂ÷ÈéT’Ðå{ž F‘¿‚ƒÃ¥òµÃÈÃËÝùÞ«ëüÃÊíæô¿ÃÍÃÍÃÉåiô»òìãÂó·ÃÏ‰ôßä›¶[ÃÔ«JÖiÃÑÃÓ÷ã÷çÃÒÞÂÃ×ÁdåôÃÔôÍëßÃÐôéœIåµÃÚÒ’ÃØÃÜƒçÖkà×ÃÛÆPÃß¾dÃÞÃâãæüwÃãííÃäÃáäÏ¾’ìtÃæß÷ÃçÃèÃéù‘èÂíðÃëÃìÃì¾˜ÃêÃ²ÃîRÑòœçÃïÃïóºÃñáº•FçäÜå¬z¾‡ÃóéhÃòãýé}‘‘Ãôœ¡œ¡öšÃûÃ÷øQÜøÚ¤ã‘àpäéêÔî¨Ãøõ¤ÃüÖ‡¿ŠÃþÖƒæÆðxÄ¡Ä£Ä¤üNÄ¦Ä¥Ä¢Ä§Ä¨Ä©š{Ä­ÜÔÄ°ï÷ÄªÄ¯Ä®ò‡õöÄ«ñ¢æŸÄ¬õøñòÄ²Ä²Ù°íøÖ\öÊÄ³Ä¸ë¤®€ÄµÄ·Ä´Ä¾ØïÄ¿ãåÛéÄÁÜÙãfÄ¼Ä¹Ä»ÄÀÄ½ÄºÄÂÆÞÖƒºÂYØ¿ÄÃårÄÄƒÈÄÇ¼{ëÇÄÈñÄâcÞàÄËÄÌÄÌÄÊÄÎÄÎÄÍÝÁØ¾Å®ÄÐ‚OÄÏëyà«ÄÏéªôö“Dœ¯ëîòï‹RàìÄÒð–êÙß­Ø«ßÎ“Ïâ®ÏuˆßÀÄXè§ô[×¿Ä×ÔGÄÅÄØðHÄÛÄÜÄÝÄáÛèâõÄàÄßâ‰â¥ÄÞöFÙ£Äã”Mì»êÇÄæÄäÄçíþÄÄéÄêöTöTð¤“ÓÝ‚”fÄëØ¥Äî‰|Äïá„øBøB‹–æÕÄòëåÄóêŸÄùðDô«ýmÂ™è‡æ‡ïDÜbÄõÞÁÄúŒŽ‡“”QªŸ™ŽÂœÄýØúôå¸âoÅ£áhÅ¤¼~¼~–ƒâoÞrƒzÄ“âÄ“Åª˜‰ññÅ«æÛñwÅ¬åóæÀÅ­Å®âSÄÍô¬Å°Å°Å¯Å²ƒ®ÖZÖZÞùåŸÅ³Å´í¥ÄèàÞÅ¶ÖŽšWšªšWút‡IÅ¼ÅºÅº‘YaâZÅö—”¬i½læq±Ùí@ÆªÆ´ÌñRûË‘·…˜ËñFùL…Ä·Â¸¬Œ´°ÅÅ¿Å¾ÝâèËÅÀ°ÒÅÃóáÅÁÅÂÅÄÙ½ÅÇÅÅÅÆ¹uÅÉÅÉÅÈÝå±eÅËÅÊÆ¬°é±PÅÍÛ˜ó´ˆmÅÐãúÅÑÅÎÅÏñÈÅÊÅÒäèý‹åÌÅÔó¦ÅÕÅÖ’ëãÅÙÅØâÒûƒÅÚÅÛÞËÅÜÅÝ°’ÅÞÅßõ¬ÅãÅàÙräžÅáÅæÅåàúì·«˜ÅäÞ\ö¬‡ŠÅèÅèâñÅêÅéÅëàØÆMÅó‚‡ÅïÅíÅï‚õ“sÅðÅîùiÅìÅñÅòó²Åõ„™ÅöØ§ÉÅúÚüÅ÷ÅûÅøâ”ÅüÅùÆ¤ÜÅèÁÅþÆ£ò·Û¯ÚðÆ¡Ûý—ÀÅýÆ¢Á`òçõùÜ±Æ¥±ÈÛÜÆ¦ñ±Æ¨æÇî¢Æ§Æ©Æ¬Æ«êúÆªôæñ‰ëÝõäÕ›ò_ØâÆ¯¿~ïhóªÆ°éèî©Æ±Æ±æÎë­Æ²Æ³ÜÖæ°Æ´Øš‹åîlïAÆ·é¯êòÆ¸Æ¸Æ¹Ù·Æ½Ôu‘{ÆºÌOàZÆÁèÒ›¯«rÆ¿Æ¼õGá•ÆÂ°lîHÆÅÛ¶ð«ØÏãOóÍÆÈçê†\ÆÆÆÉÆÇÆÊÅàÙöƒWê·“ääàÛõ‹ÆÏÆÎÆÐÆÏÆÑè±å§çh˜ãÆÔÆÒÆÖÆÕäß×Vë«ç’õëÆØúIÓH¬gñÊ½®äÐþôòöÄÆßÆãÆÞÆâœD—«˜ÆÝÝÂÆÚÆÛÆÝéÊÆáõèØÁÆîýRÛßáªÜÎÆäÆæÆçÆíêÈÄší ÆéÆüÆèÝ½òUòTÆåçùç÷ì÷Ï“ÆìôëòàÐ½ö’÷èÆòÆóá¨ØMÜ»†™è½Æð¾_ôìšâÓ™ãàÆù—‰ÆûÆüÆõÆöËjÝÝ´ƒÆ÷í¬ÆþÝÖÇ¡Ç¢÷ÄÇ§ÇªÚä’LÜ·ßwƒLá©âT ¿‘aãUÖtí©ºžòqåºå½Ç°âjò¯åXãQÇ¬Þçóé“Ç­œ\Ädã»Ç²×lÀ`Ç·Ç·ÜçÙ»‰qÇ¶˜ Ç¸†ÜÇ¼ãÞ‘ê˜ŒÛ„Ç»òÞäçIçjŠ  ËN™{“ŒÁu¿‹ŸÍÇÄ´“ÜEàƒØäÇÃæ@ÇÁÀRÜF˜òƒSÊw˜ò×Sã¾íXéÔÇÆÇÉã¸ÇÎÕVÇÍ¸[ÂNÇËÇÊÇÐÇÑÇÒæªÇÓ¸`êüÜºDå›ÓHÇÖšJôÀôÜËÇÛÇØÇÙÇÝÇÚàºäÚÇÝÇÜÇÜòû½þŒ‹†wÇß“åÇàšäÝpƒAÇäÇàÇåòßõ›ÇéÇçÇèÇæ¾¯÷ô™”í•Õˆö¥‘cóäíàóÀõ¼öÆÚö¸Fñ·Ÿ¦óÌ­‚òËÇðÇñÇïòÇÈcé±öqÇôáìÇóÍAÇöÙ´ÇõÞåÏÇòÙgŽ€åÙôÃòø÷üôÜ…^Çú…^ÔxòŒÇüìîÇùÜ|òÐÚ…ôðôð÷ñÛ¾ëÔøzÇþÞ¡íáè³Þ¾ë¬ÄŸáéó½È¡È¢ýxÈ¥é˜ÓUÈ¤ãªÈ¦çzÈ«™àÔÈªÜõÈ­ÝbÈ¬ãŒóÜòéÈ©÷ÜïEÈ®î°¾J„ñÈ¯È²È±È³…sâÈ¸´_é êIùoÈ¶ËóÈ¹ÈºàSòÅÈ»÷×È¼È½ÜÛÈ¾žìüÈ¿ð¦ÈÂÈÀÈÁ×ŒÊðˆ˜ï”_‹ÆÀ@ÈÇŸáÈËÈÊÈÉÈÌÜóïþÈÐÕJØðÓ•ÈÎ¼xÈÑÜígïƒñÅÈÓÈÔµiÈÕÈÖëÀáõ½qÈ×˜sÈÝŽVÈÜÈØéÅÈÛÏ”ægÈÚÈÚÈßÈáÈàôÛõå÷·ÈâÈçÈããœÈåàéÈæÈåÞ¸ñàÈäîžÈêÈéÈèàrÈëÈçäá¿dÝêÈìÈîÈîÜ›Þ¨ÈïÜÇèÄò¸äJÈðî£écécÈôÙ¼ÈõàeÈôœcóèè¼Ë¼·_²Ù“½¾D³×ÝçiÝØØíÈöž¢Ø¦ïSëÛË_ÈûÈùÈûöwÙÈýÈýšÐ‚ãÉ¢¼Rð€É£É¤Þúíßî‹†ÊÉ¦ò}¿‰ëýö…’ßÉ©Ü£ðþÉ«­†ÝäC¬XÉª·wÉ­É®š¢É³¼†É°É¯æ|ððôÄõÉµßþÉ¶¿³É·ö®ºY•ñÉ½Èý„hÉ¼ÜÏŠ™ÉÀáŸÛïÉºô®õÇÉ¿äúëþéWê„Ó˜ÉÇðÞÉ»ÉÈÉÆò~Û·¿˜æÓÉÃÉÅÙ óµ÷X‚ûš‘ÉÌÓxÉÊìØÉÑˆsÉÎÙpÉÏÉÐ¾yÉÓÉÒŸýÉÔóâô¹òÙÉ×ÉÖÉØÉÙÛ¿ÉÛÉÛ½BÉÚäûÉÝâ¦ÙdÙÜÉàÙÜÉßÉá…‡ÔOÉçÉäÉæÉâ‘Ø”zž—÷êÉêÉìÉíêÉë¼Ô–«|ÉïÉéÉîÉñ³ÁŒßÓïòÕ”‹ðžcÄIÉõëÏBÉ÷é©ò×ÉýÉúÂ•Éü„ÙóÏÉûÀKÊ¡íòÂ}êÉÊ¢Ê£áÓÊºÊ§ŽŸÔŠßŸÊ©ª{ñá‡Ê®Ê²Ê¯•r×RŒÊ°ÎgÊ³Ê·Ê¸Ê¹Ê¼ñ‚Ê¿ÊÏÊÀÊËÊÐÊ¾Ê½ÊÂÊÌ„ÝÒ•Ô‡ï—ÊÒÊÇÊÁß@ÝYÊÅÒæáŒóßÊÄŠ]ÊÕÊÖÊØÊ×ô¼‰ÛÊÜá÷«FÊÛÊÚ¾RÊÝ•øì¯Êã¼‚Êå˜ÐêxÊâÙ¿ÊâÊáÊçÝÄàgÊèÊæ”dë¨Ý”ÊßïøÊëÚHÛÓÊì­qÊîÊòÊðÊóÊñÊíÊïÐgÊùÊøÊöÊö˜äØQË¡Êü”µëòÊûÊþäøË¢Ë¢Ë£Ë¥Ë¤Ë¦Ž›ó°éVË©Ë¨äÌëpËªæ×Ë¬ÕlË®¶Ë¯Ë±í˜Ë´Ë²Õfåù qË·èp´TàÊÞ÷ÝôËÔéÃÛÌ½zË¾Ë½‡zË¼úƒË¹¾ŒÎ‡æJË»ËºäùËÀËÈËÄËÂÓ›ËÅËÆ‚HÙîËÆìëãôï•ñ†Ù¹óÓñêËÃËÁòIâìËÉŠ»Ú¡áÂäÁËÉáÔ‘Zã¤Â–ñµÔAËÎÕbËÍížà²ËÑÉ©ðtï`æ}ËÒòôÛÅàÕî¤”\”µÌKËÖ·dË×«TÙíÔVÃCä³ËØËÙËÚÖqà¼ËÜãºËÝËÛÝøö¢­Xóùâ¡ËáËâËãëmÝ´íõËùî¡å¡½—ËåëSËèšqËîÕrËìËéËíìÝËëåäŒOªsÉpâ¸“p¹SöÀé¾Ëôæ¶êýèøËóíüàÂôÈËò¿sËù†îË÷¬¬BÝ·ñâ–ÅŒÙ²_³Êk‚mÌ¤Ì­ cÕ{â^¶ÚËýÜæËûËüõÁãBËúäâËþ«Hö“éêYåÝé½Ì¤Ì£ñ~Ì¥Ì¨Û¢Ì§Ì¦ìÆõÌõTÞ·Ì«Ì­‘BëÄâÌ©ÌªÌ®Ø”‚ž©°c‰¯•ÒÕ„Û°ñûÌµåU×TÌ¶Ì´ìþÌ¹Ì»Ì¹ÌºšUÌ¿Ì½Ì¼œ«ç|ôÊçMœ«ÌÆÌÃÌÄÌÁÌÂÌÁÌÆéÌÌÅÌÇó¥ó«õ±àûÌÈÌÊƒ¯ñíÌÉ CÌË|ý¿lÌÍþÌÏíw÷ÒÌÒÌÓÌÒÌÕßû™„ÌÔÌÑ™„ìŠÓ‘Ì×ìýß¯ÌØäˆí«ÌÛòvÖ`ëøÌÙÌÞÌÝäRÌß½ÌÝÌäÌá¾ŸùYzî}Ìãõ®ówŒÏÌêÙÃã©ÌéåÑÌèÌæñÓÌçÌìÌíÌïÌñî±ÌðÌîêDãÃéåœL¬_ÌóÌòÞÝÙ¬Ìôìö—lÌöóÔýfµñ÷Øöœñ»Ì÷¼gÌøÙNÙNèFÌû÷ÑdÍ¡Â ŸNÍ¢Í¤Í¥ÜðÍ£æÃÝãòÑöªÍ¦èè¬EÍ§Í¨Í¨ÙÚÍ¬Ù¡Í®Í²Í©íÅã~Í¯ÍªÍ¯äüÍ«½yÍ±Í°Í²‘QÍ´Íµî^Í¶¹ÉÍ¸Í¹¶dÍ»ˆD‰TÍ½‰TÝ±Í¾ÍÀõ©ÍÁÍÂâQÍÃÜ¢ÝËÍÄˆFŒ£î¶åèÍÆîjÍÈÍËÍËÍ‘ÍÊÍÌêÕÍÍï‚ëàÍÎÙÛØ±ÍÐÍÏÃ“ñWÙ¢ÍÓÛçãûñ„èÞíÈørõÉõ¢éÒüƒÍ×âÕ™EÍØèØÍÙ»Xà„ÍÛÍÞÍÚ¸D‹zÍÜÍßØôÒmÄeÍááËÍâØàž³òêÍãÍè¼wÜ¹ÍêÍæîBÍéÍðÍìÍíÍñÍï¾UëäÝÒçþÍîîµÍëÈfÍóÍôÍöÍõ¾WÍùÍ÷Øèã¯Ýy÷ÍÍýÍüÍúÍûÎ£ÍþÙËÎ¯ÚñÝÚÎ¢ìÐÞ±Î¡ ‘íf‡úŽ®‚Îß`éÎ¦¬Î¨á¡Î©¾SáÍœ‘žH‚¥ƒ^Î²¾•È”Î¯Ÿ˜¬|ÓÐæ¸Õ†Î®À¢â«ÉJðôôºítõnÐlÎ´Î»Î¶Î·Î¸ê¦Î¾Ö^Î¹Î¼ÎoÎµÎ¿ÎºœØÎÁÎÄ¼yÂ„ÎÃé”ö©ØØÎÇÎÉ·€†–ãëè·ÎÌÎËÝî®YÞ³“ëÙÁœuÈnà¸ÎÎÎÒÎÖë¿ÅPÎÝá¢ÎÕä×íÒÎÓý}žõÛØº¹àw†èÎ×ÎÝÕ_æuŸoÎã…ÇÎáÊÎàä´òÚ÷ùÎåÎçØõÎé‰]‹³Tâè‘“åÃÎäÎêÎæêõ« ù^ÎèØ£Îð„ÕÎìÚã’Nè»ÜÌÎïÕ`ÎòÎîìÉæÄðíò\ìFå»úFöÈšGÎüœä¸ßàAÓiÛ¨Ï¾¼Ùàåá—«ÊnÀ]ÏÈËÞ’Û×Ï¦ÙâÏ«Î÷ÎüÏ£ÎôÎöÎùñ¶Û­Ï£ÞÉŠÖÏ¢•„Ï¡ ÞÏ¤Ï§ì¤ÕãÏ©ÎøÕÛ‚ÝÎúÏ¬Ï¡ôÑôâô¸ÏªðªåaÙÒÏ¨ÎõòáÎûæÒÏ¥éØì¨ìäôËó£ Oó¬ØGëvõµêØ÷ûÁ•Ï¯ÒuÒ Ï±ÚôÏ­Ï´­tÍ½ãŠÏ²ÝßåïÝûÏ²÷^‘òÏµðq¼šàSô]ôªÏ¶ìùÎrÏ¹Ï»‚báò{èÔªM³ˆåÚÏ¾è¦Ý Ï¼÷ïÏÂ‡˜ÏÄóÁÏÉÏÈÀwë¯ìì¶iËWÏÆÜ]õµÏÆõråßÁéeÏÒÙtûyÏÑéeÏÏã•°Bú‘ÏÓÏ´ï@ëUªÍ˜óÚõÐÌ\ìÞ¿hsÇ{¬F¾€ÏÞ‘—ÏÝðW¾€Áw«IÏÙ¾€ö±àlàlÏàÏãŽûÏæ¾|ÏäÏäÏåóJè‚Ô”âÔÏéÏèÏíí‘ðAð‹ÏëõœÏòÏïí—ÏóÏñÏðó­—nÏ÷•ÔèÕò”ÏüÏû½‹åÐÊ’ÏõäNžtºÏö÷Ì‡ÌáÅÏýÐ¡•ÔóãÐ¢Ð¤ÏøÐ§Ð£Ð¦‡[Ð©Ð¨ÐªÏ…fÐ°Ã{ÙÉÐ±ÖC”yÛÄ”XÀiÐ¬Œ‘Ð¹žaÐ¹Ð¶Ð¼‚ÄÐµÒCµúÖxéÇé¿âÝÐ¸â³Þ¯åâÛÆå¬Ð·õóÐÄß”ÐÃÐ¾ÐÁê¿ÐÀä\ÐÂì§Ð½Ü°öÎØ¶ÐÅá…ÅdÐÇÐÊÐÉÐÈÐÌÐÏÐÎê€ÐÍíÊÐÑß©ÐÓÐÕÐÒÐÔÜôŠüÐÔ›ëÐ×ÐÖÐÙÜºÔKÐØÐØÔKÐÛÐÜÐÝÐÞÐÝâÓÐßø õ÷ð}÷ÛÐàÐãá¶ÀCÐäçnçn³ôÐçíìÐöíšÓ’íœÌ“šH‡uÐèÐæÐìÐìÔSÔ‚èò«ôÚÐñÐò”¢ÐôÑªÛÃ¾wÀmÐïÐö”¢ÐõÐáìãÐîÞ£ÜŽÐûÖXÐúÞïÝæêÑìÓ²UÙØÐþ«tðç‘ÒÐýäö­vßxŸ@°_ãùìÅ½kÑ£ãC¬KäÖé¸ïàæ›Ñ¥Ñ¦Ñ¨ŒWÍõ½Ñ©÷LÑªÖo„ì‰_Ñ¬ñ¿â´Þ¹êÖõ¸Œ¤Ñ²Ñ®ñZÔƒá¾âþÑ®Œ¤ñZÜ÷«‘Ñ­à‰÷\Ó–ÓÑ´Ñ¸áßßdÑ³ÙãÞ¦»nìa‹jéœ••‰¥Ó”ƒe‚\Šxç~åWØß›âÍë¬^àN›ªÔDÃ‘Ñ¾‰ºÑ½Ñºøf—¿ø†ÑÀØóá¬Ñ¿…ƒçðÑÁÑÂÑÄíýÑÃ†¡¯{ÑÅ†Ó åÂˆº‹Ií¼šåÞëÑÊ‘ÃŸŸëÙáÃÑÍÑÉÓÙéŽÑÝáZƒBÛ³æÌÑÓéZ‡ÀÑÐÜ¾ÑÔÑÒÑØÑ×ÑÐû}éóÛÑÑîº™ƒ¼ÑÙƒ°ÑçÑÜÙÈ…˜ÑÚÑÛçüî»ÑÝô|ýB…’©³ŽÑäÑçêÌØWòžÖVÑßÑæ»ðÑãØWá‰×—ÑàÚIÑàÑëãóÑêÑíø„÷±“PÑòê–•ª—îŸ¬Ñð¯ƒáàÑóìÈ¤òÕ•ªÑöÑöðBÑõ°Wâóí¦ÑóÑúÃ´Ø²Ò§ÑýÑüÑûØ³ˆòëÈÒ¦ÝUçò¸G‚çÖ{áæ“ußb¬ŽôíöŽèÃÒ§ñºÒ¨ÑüËŽÒªú_ê×Ò«Ò¬Ò­ ”Ò®ÞÞäyÒ²Ò±Ò°˜IÈ~Ò·í“à’Ò¹•ÏŸîÒ´ÒºÖ]Ò¸ìvÒ»ÒÁÒÂátÒÀ¶BßÞâ¢ãžÒ»Ò¾ì¥äôàæ÷ðƒxÛÝÒÄÒÊÔrÒËâùåÆïß×ÒÌÜèÙOíôÒÈôýðêÒÆÔrßzîUÒÉáÚÒÍÒÒÒÑÒÔáÌÒÓÜÓÅœÏÒÐÒÎì½Áxƒ|ß®Ø×‘›Ë‡Øî×hÒàÒÙ®Øý‡ÒÒÛÒÖ×gÒØÇÄŽF‘«Ò×À[Ô„óAÞÈÞÄÒßôàÝWã¨ÞÚÒæÕxÛüñ´ÒîÒÝÒâÒç¿OÒÞÒá¯ŽòæÒãìÚæ„Øæéì DÞ²ôèÒíÒÜñ¯èOÀXž‹Ü²ÒòêŽÒöÒòÒðÊaÒôÒóë³ãŸà³Ü§Ò÷Ûóáþ«ÒúÒùãyÛ´Òúýlö¯ÒüÒýÒýï‹ò¾ë[°aÓ¡Ó¡Ø·‘ªÓ¢úL‹ëÓ¢çø‡Â”tÀtÀ›™Ñ™ÑûWâßú—Ó­‰LÓ¯œîŸÉ¬“Îž I¿Méºž]ævžuÏ‰ÙøÚAå­Û«}·fÓ°°`Ó³Ó²ëô†ÑÓý‚ò“í°bçßÓ¹à{ÓºÜ­ã¼ÛÕÓ·÷I÷ÓÓöÓÀð®ÔÓ¾Ù¸ÓÂÓÂÓÁÓ¼ÛxÓÃƒž‘nØüßÏÓÄÓÆÞÌÓÈÓÉªqà]ÓÍèÖðàÉ‰Ê~â™òÄß[ôœéàòöÓÑÓÐØÕÓÏÁhÐãäBë»÷îÓÖÓÒÓ×ÓÓÙ§àóå¶ÓÒÕTòÊÓÔ÷øì¶ÓØÓÙÓåðöÓÚÓèßŽðNæ¥šeÅcì¶ÓÛô§ô~ÓáØ®óÄô¨ŠÊáüÕ˜ðNô~ÝÇÓçö§£ÓäÓáëéÓâÓÞÓÜè¤ÓÝÓDñ¾Ý›òõÅc‚øÓîŽZÓðÓê‚RÓíÕZÎáÐÒâ×àhÈfðõñÁýrÓññSí²Óó‹žï„ÓýôdªêÅªzÓøÔ¡â•îAÓòÓûœUÖIé“Ó÷Ô¢¶R¬ZÔ£ÓöùOÓúìÏÊš×uØ¹òâÔ¥ìÛú–å÷øSÔ©íóøxœYóîÔª†T†TÔªÔ«ë¼Ô­ˆAÔ¬Ô®¾‰üxÔ­Ô´Ô³Þ@™´ó¢ßhÔ·Ô¹ÔºæÂÔ®îŠÔ»¼sÔÂë¾µjŽ[è€‚ãXé†ÜS»›Ô½Ô½Ùßå®ügë…„òŠu›V¼‹Ü¿êÀàyÔÅšèÔÊÔÊëEëEÔÐß\àiÁ•žáj‘C¿ZíyíìÙÌNÔøÛ‚Æƒ]éLü{Ãq½‚¼—“oÇïódËF´ËÚõ¡™çà©ºd„–ÔÑÔÒÞÙësÔÒžÄçÞÔÕÔÔÇÔ×ÝdáÌÔÙÔÚôØô¢ÔÛêÃ”€ƒ­ÚŽ•ºÙçY­”€ÚEê°ñzÞÊÔáÔáÔâÔãèÔç——ÔéÔèÔåÔîÔíÔíÔìÔëÔïÔê„t“ñÉØŸåÅ‡KŽ¾óÐô·ºjÙ‘ØÆê¾Ù\Ôõ×PÔöÔ÷¿•îÀä{êµÙ›ß¸Õ¦ßîÔû“«Ôü²éýOýO¼™„žÜˆélåŽÕ£íÄÕ§Ôpß¸Õ¨ðäòÆÕ¥ýSÕªÕ¬µÔÕ­‚ùíÎÕ¯ñ©Õ´šÖì¹Õ³Õ²×dÕ°÷g”ØÕ¹±KäÞøÝšÕ¼×‘ð—£Õ¾¾`Õ¿ÕºˆÕÂÛµæÑÕÃÕÄâ¯ÕÁè°ó¯ØëqÕÆÕÉÕÌŽ¤ÕÈÃ›Ž¤ÕÏÕÂá¤ÕÎá“ÕÐÕÑßúÕÒÕÓÕÙÕ×ÔtÚwóÉ×ÀÕÕÕÖÕØÃDòØÕÚÕÛÕÜÝmÏUÖ†ß¡íÝÞHÕßÕßô÷ñÞß@èÏÕãÕáúpØ‘á˜‚ÉØ‘Õä˜EÕæÕèµÕåÕçÇØé»óðÕéæPÔ\ÕíëÓÝFî³Õî¿bð¡ÛÚê‡øcÕñëÞÙcæ‚Õð ŽÕ÷Õ÷Õú˜’êªbã`± åP¹~ÕôáçÕüÕûÕý×CÕŠàŽ¬ÕþÖ¢Ö®Ö§Ø´Ö­Ö¥Ö¨Ö¦Öª¿—Ö«—dìóëÕÖ¬ÖÁÖ©ˆÌÖ¶Ö±ÖµÛúÂšÖ²Ö³¿{Û•ÞýÜUÖ¹Ö»Ö¼Ö·›b¼ˆÖ¥ìíåëÖ¸¿—ÂšÖºíéõ¥ÖÁÖ¾ÆçõôÖÆàùŽÃÖÎÖËÙ|Û¤ÖÅ™±Úì“´•yèäÖÈÖÂÙ—Ýe”SÖÌÖÏúvåéÖÇœþðëòÎòsÖÉÖÃÖÉëùÓzÜW­}ÖÐŠqÖÒ½KÖÑæRô±ÖÔæRó®Ä[·N‰VõàÖÙ±ŠÖØÖÝÖÛÖaÖÜÖÞàXÖàæ¨ÝSíØÖâÖã¼qÖäÖæ¿U•ƒëÐÈ’°™ôüóEô¦ÖìÙªÕDÛ¥ä¨ÜïÖêÖéÖTØiãÖë™½žz™ÁÖñóÃ TÖðô¶ðñõîÖ÷ÖôÁCÖTÖó‡Ú÷æ²šÐ×¡ÖúÆrÊã×¢ÙAñvÖùÖ÷×£ðæÖøÖûºB×¢èTóçôã×¥×¦×§Œ£´uî…ÞDÞDÙ×«×­ð‚ŠyÇf˜¶Ñb‰Ñ î´±×²‘Þö¿×·òK×µåF‰‹¾Yã·¿PÙ˜ëÆñ¸Õ»´×¿×¾Ù¾×½×À—‡äÃ¬k×Æ×Âí½á×½ÕŽ×Ã×ÄÖø×ÁìúßªåªèCž•×Ð×ÎÆ×É×ËÆÙDÙY×Í¾lÖJæÜáÑ×ÌÝw×ìôôåOýb÷Úõ™×Ñ×Óæ¢ïöñèóÊè÷×Ï×Òö¤×Ö×Ô×Én±{×Ú¾C×ØëêÛ™××¿‚‚ô¿vôÕàuò|ÕŒà‡ÚîÛ¸öO×ß×à×á×âÝÏ×ã×ä×å‚úæ—Ô{×è½MÙÞ«~×æÜgÀy×ëèß¬×ì×î×ïÞ©×í×ð×ñé×ç÷VƒVß¤×ò×ó×ô×÷×øÚèÕ¨×õìñëÑ×ø×ù×ö“Q";
    private static final String jianti = "°¥°§°¦°£°¤ïÍÞß°¨°©àÈ°«°ªö°°¬°®íÁ°¯àÉæÈ°­êÓè¨ƒvËB°²°¸°±âÖÚÏÄWðÆ°°°²±V†±Ûû°³°³Ûûï§Þîáí°¶°´°¸°·°µ÷ö°¹°º–‹°»ál°¼Ûêˆ n°½à»åâÚéáåÛ°¾­H°¿ñúòüÖ’÷¡÷éÆb°ÃæÁá®°ÁŠS°Âæñ°Ä°ÃöË°Ë°Í°È°Ç°Ç°ÉŠBá±°Å°Ì°Æ°ÊôÎØ^÷„°ÎÜØÃ_°Î°Ï÷É°Ñ°Ð°Óy°Ö°ÕöÑ°Ôå±êþ°×°Ù°Û°Ø–àÞã°ÚßÂ°Ü°Ý°Þ°â”‘°à°ã°ä°ß°á”Ê°ã°ßÛàÛà°å°æîÓô²°æô‘°ì°ë°é°çŠ”°è°è°ê°î°ï°ðäº°î°ñ°ò°ö°ø°ô°ù‰Y¶œÝò°õ°÷Ùè°üæß°ú°ûìÒöµ°ý±¢±¦±¥±£ð±±¤ÝáñÙ±¨±§±ªõÀ°û±«±©Ì™±¬Úé±°±­±¯±®ðÇ±±±´±·Úý±¸±³±µ±¶ã£±»‚³±¹±º±²íÕÝíñØóc¼L÷¹öÍ±¼êÚ‚–ï¼±¾±½ÛÎÛÐ±¿È±À±Á±Ä±ÂÈE¬e±Ã±Åê´±À±ÄŒÂ±Æ÷”–ÄÝ©±ÇØ°±ÈÓßÁåþ›a±ËïõÙÂ±Êô°±É±Î±Ø±Ï±Õ±Ó×ß›î¯ßÙ±Ñ«Üê±Ý±Ðáùîéæ¾âØ±Ö—a±°ˆãåöÏã¹°zóÙ±´ääŸ•±Ô±Æ±ÍÆ¢õÏés±×±Ìóë±ÎñEñƒó±ÚæÔó÷ÞµÓv±ÜõIå¨±Û÷ÂŠ`èµð{ôÅÒgÜLú‡±ßí¾±ß±àìÔ¹òùöý±Þ±á±âñ¹ØÒÆ±â·HñÛËx±åÛÍâí’\›MãêÜÐ±ã±ä•c±àçÂ±éÅŒ±æ±ç±ç±ë±êì©ªYŽ¼±ëæôŸÏ±ìñ¦ïÚì­ì®ždïð±íæ»ñÑÒF™~‚l÷§±ïü‚±ðõ¿±ñß“±ö±òÙÏ±ó±öçÍéÄïÙ±ôÓÄÏ™±÷éëë÷÷Æ÷Þ±ù±ø–Þ±ûÚû±ü±ú±þ±ýÍsÙ÷²¢ŽÕ²¡Þð²¦²¨²£°þÑB²§âÄà£²±²¤ó²¥²®ØÃ²µ²¯²´­“²ªÙñ›Âîà²¬²°²©²³È•ð¾²«ñA÷ˆƒk²­²²õÛéD±¡ÒqíçõËô¤ë¢éÞ²¶ŽïÆÒîßêÎðJõ³²·²¹²¹²¸²¶²»²¼²½²ÀîÐ²¿²ºê³²¿º^²¾·Ôº»²¡ÆÙàê²Áíå²Â²Å²Ä²Ä²Ã²É²Ê²Ç¾Z²È²Ë²Ì¿n²Òæî²Í²Ð²Ï²Ñ²Ò‘”÷õ÷õ²ÓôÓè²²ÖØ÷²×²Ô²Õ²Øè†²Ù²Ú²ÜàÐäî²Ûô½ó©²Ý²á²à²Þ²â²â”˜²ß¹‹á¯—qä¹àá²ã²ä²æè¾Åa²è²åâÇïÊÅ‘ˆ“²é²ç²è¿²ëâª²î²ì²êéßñÃ²ìãâ²í÷²ïæ±²î²ðîÎÙ­²ñ²òò²ðûêè²ô²óŽÊæ¿²÷åîìø²ö²ø²õäaâÜäýïâ²øó¸Žfõð²ö®b„i²úÚÆ²ù²ûÝÛÙæÀAâã²üåñí]Øö²ýæ½²þÝÅãÑ¬döð³¦ÜÉ³¢³¥³£áäæÏ÷•³§³¡êÆã®³¨ë©äâê«`³©³«ÛË³ª³©ío³­€â÷³®´Â³¬êË³²³¯³°³±³³³´±|ûžñé³µíº†q³¶³·Ûå³¸³·³ºÞÓ³»è¡àÁ³¾³¼³À³Á³½³Â³½³¿ÚÈÚ’í×‰}Û{´³³Ä¯M³Æö³³Ãé´Úßèß‚ òÉîõ ª¬bÚW³ÅîªØ©³É³Ê³ÐèÇ³ÏàJ³ÇŒk³ËÛôÃ”îñ³Í³ÌñÎëóõ¨³Î³È³Ñ³Ò³Ó³Ô³àò¿ð·®Eí÷ó×àÍæÊ“¤³Õó¤÷Î³Ú³Ø³Û³ÙÜÝ³ÖÜ¯õØóø³ß³Þ³Ý³ÜôùñÝáÜß³³â³àâÁ’x³ã³áë·à´ÙÑ‘yñ¡³ä³åâç³äÁˆ´º‘oã¿ô¾³æ³ç³èï¥³éñ¬ºN ß³ðÙ±³ëã°³ñ³ë³î³í³ï³ê³ì‘ÀöÅ³ó³ò³ôšŽ³ö³õ“¹éËÛ»³ý³÷³ü³úòÜ³û³÷ŽÐ³ùõéèÆ´¡´¢èú³þñÒýƒØ¡´¦ãÀ´¡¬`´¤´¥ãÀšb÷í´£ÞõÄu´§à¨àÜõß´¨´¨ë°´©´«ô­´¬´§´ªšNâ¶ÇF´­ƒb´®îË´°´¯´°“œ´²‡l´´âë´µ´¶´¹Úï´·Ç”é¢é³´¸´º‰@´»òíöj´¿´½Ý»´¾ðÈ_´¼Ûw´ÀõÖ´ÁÞu´Ù´ÂßOê¡öºšfßÚ«u´Ã´ÊìôÜë´Ä´É´È´Ç´Å´ÆðËôÙ´ËÕ´Î´Î´Ì´Í´Ó´Ò´Ó×Ý´Ðæõè®´Ï´ÔäÈ×Ø´Õ×àëíê£´ÖáÞéã´Ùâ§×å´×´Øõ¾õíÙàß¥ïé´Ú´Ü´Ûìà´Þ´ß´ÝéÁè­´àßýã²´ãÝÍÇË´á´â´ä´åñå´æââ´ç´ê´è´éõãáÏðîïóõºëâÎô´ì´ëï±´íÉ²ÐóÔøëúå¤æö×ÊÒbÑnº@´¼ßÕÞÇ´îËþñ×´ïæ§âòóÎ´ð´ñ÷°÷²´ò´ó´ô´õ´ö´úá·ß°çªåÊ´ø´ýµ¡´ùçé´ûÜ¤´ü´þ´÷÷ìµ¤µ¥µ£íñµ¢µ¦ñõééð÷óìÙÙµ¨ðã¼µ§µ©µ«µ®à¢µ¯µ¬µ­ÝÌµ°µªµ­µ±ñÉµ²µ³ÚÔÛÊå´í¸µ´µµÝÐµ¶µ¶âáë®µ¼µºµ¹µ·µ»µ¸µ½µ¿µÁµÀµ¾ôîµÃï½µÂµÄµÆµÇàâµÅµÅµÈ´ÁµËµÊáØµÉíãïëµÍôÆµÌµÎµÎïáíLµÒÙáµÏµÐµÓÝ¶µÑêëµÕØµÚ®Û¡Ûæµ×µÖèÜíÆ÷¾µØµÜ«ZµÛæ·µÝµÚÚÐé¦íûµÞµÙíÚµùµàµáµßáÛñ²µäµãµâõÚµçµèµéÚçÛãµêµæçèîäµëµíµìµîñ°ô¡µóµðµñõõµïµñöôµõµöµôµõµùµøµüÛìµøµýÜ¦ÞéñóµüëºµúµûõÞöø¶¡Øê¶£¶¡ðÛ¶¢¶¤ñôôú¶¥¶¦¶©¶¨à¤ëëíÖ¶§¶ªîû¶«¶¬ßË¶³ð´¶­¶®Ê¶¯¶³¶±Ûíá¼¶²¶°¶´ëËëØíÏ¶¼¶µÝúóû¶·¶¶¶¸ò½¶¹àK¶º¶»ñ¼¶¼¶½¶¾¶Á¶¿èüë¹¶¿÷ò÷Ç¶ÀóÆ¶Â¶Ä¶ÃÜ¶¶Ê¶Å¶Ç¶È¶É¶Æó¼¶Ë¶Ì¶Î¶Ï¶Ðé²ìÑ¶Íóý¶Ñ¶Ó¶Ô¶Òµq¶Ô¶Ñí­ïæ¶Öª¶Ø¶Õíâ¶×íïõ»¶ÖìÀ¶Üí»¶Û¶Ù¶Ý¶àßÍ¶ßñÖ¶áîì¶Þ”£õâ¶äßá¶âç¶¶ã¶çãõ¶é¶æ¶è¶åêæï¢¶¤îú³j¿Éðâ¶ï¶í¶ð¶ëÎÒï°¶ì¶ê¶î°¢¶òêi°¦¶ó¶òéîÛÑ¶ñ¶öÚÌ¶õãÕãµÝà¶ôëñïÉðÊò¦Ø¬ÚÌöù¶÷ÝìÞô¶ù¶øð¹öÜ¶û¶úåÇ¶ý¶üçíîï¶þÙ¦·¡ßíÚÀü–µp‚¿·¢·¦·¥ÛÒ·£·§·¤·¨íÀ·¨·«·¬á¦·­·ª·²·¯·°·³·®Þ¬ìÜ­[·±õì·±ÞÀçx·´·µ·¸·¸·«·º·¹·¶··î²èó·½Úú·»·¼èÊîÕ·À·Á·¿·¾öÐ·Â·Ã·Ä•Pô³·Å·Éåú·Ç·ÈŠóç³·Æìéòãö­öî·ÊäÇ·Î·Ë·Ì·Çì³é¼ôäóõ·Í·Ï·Ðáô·Î·ÑðòïÐ·Ö·Ô·×·Ò·Õ·Ó·Ø·Ú·Ò·Ù÷÷·Û·Ý·Ü·ÞÙÇ·ß·àö÷å¯·á·çãã›h·ã·â·èí¿·å·éÝ×·æ·äÛº·ë·ê·ì·í·î·ï·îÙºó¾·ñ·òß»·ôõÃôïïûõÆ·õ·ó¸¥·üÙìæÚ·öÜ½ÜÀâö·÷·þç¦ç¨·û·ý–¢·úìðî··üÛ®¸¡íÉÝ³ò¶Ùëèõ¸¢¬M·ûåõÝÊ¸¤·ù¸£·øá¥òðíê¸§¸¦¸®ÞÔ¸«¸©¸ª¸¨¸­äæ¸¯íë¸¸¸¼¸¶¸¾¸º¸½¸À¸·æâÑ}¸°¸±¸µ¸»¸³¸¿¸¹öÖêçÎlòóöû¸²ð¥ÐñÙ¤îÅæÙ¸Â¸ÁæØÞÎ¸Ãº¢Ûòêà¸ÄØ¤¸Æ¸Ç¸Èê®¸Å¸É¸Ê¸É¸Ë¸ÎÛáãï¸Ê¸Ì¸ÍðáôûÞÏ¸Ñ¸Ï¸Ò¸Ðä÷éÏß¦êºí·ç¤äÆ¸É¸Ó¸Ô¸Õ¸Ú¸Ù¸Ø¸×¸Öî¸¸Û¸Üóàí°¸Þ¸á¸ßØº¸à¸Ý¸âê½¸ãçÉÞ»¸å¸ãÞ»¸æÚ¾Û¬ï¯¸êÛÙæü¸í¸ç¸ìñË¸ë¸î¸é¸è¸ó¸ï¸ñØª¸ð¸ôàÃÜªë¡ëõïÓ÷ÀºØô´¸ö¸÷ò´íÑ¸õ¹w¸ù¸úºÝØ¨¸ù¸ú¸ü¸ý¸ûâÙ¸þàQÓ²¹¡ç®¹¢¹£öá¹¤¹­¹«¹¦¹¥¹©ëÅ¹¬¹§ò¼¹ª¹¨ö¡¹®¹¯¹°¹°¹²¹±¹´¾Ð¹³âhçÃçÃóô÷¸á¸¹·¹¶èÛ«vóÑ¹¹Ú¸¹º¹¸¹»¹¹ì°åÜêí¹À¹¾¹Ã¹Â¹Áéïð³¹½¹½òÁõý¹¼ôþì±¹¿÷½¹Åãé¹À¹È¹Éêô¹Çî¹¹Á¹Æ¹ÄØÅ·Yëûî­¹Ì¹Ê¹ËáÄèôêö¹ÍðóïÀöñ¹Ï¹ÎëÒð»¹Ï¹Î¹ÑØÔÚ´¹Ò¹Ó¹Ô¹Õ¹Ö¹Ø¹Û¹Ù¹ÚÙÄ¹×÷¤¹Ý¹Ü¹á¹ßÞè¹ÜîÂ¹à­ðÙ¹Þ¹âßÛèæë×¹ãáî¹ä¹é¹çæ£¹ê¹æð§¹ë¹è¹åöÙå³¹ìâÑ¹ì¹î¹ï¹íêÐóþ¹ôØÛ¹ñêÁ¹ó¹ð¹ò÷¬Ùòçµ¹÷¹ö¹ööç¹÷ßÃÛö¹ùáÆñø¹øòå‡ë¹úàþÞâë½Ùå¹ûâ£é¤òä¹ü¹ýèí¿©Ý¸Ñºðgì¸òðÀ¹þ¹þº£º¢º¡º£ëÜõ°º¥º§º¦º¤ñüòÀº¨º©÷ýÚõº¬ºªº¯º¬êÏº­ìÊº®º«º±º°ººº¹ºµº·º´º¸¬HÝÕ‚þò¥Þþº¶º³º²å«º¼ç¬º½ñþãìÝïàãÞ¶òººÁàÆºÀº¿º¾ºÀºÃºÂºÅê»ºÆºÄœBð©ò«å°å°Ú­ºÇºÈºÉºÌºÏºÎºËºÍºÓºÈºÒºËîÁºÉºÔºÐºÊÔZò¢ãØôçÞºØºÖºÕº×ÛÖºÚºÙºÛºÜºÝºÞºàºßºãèìçñºáºâºâºäºåÙêºæÞ°ºëºìºêãÈãüºéÝ¦ºçºèÞ®ÙäÚ§ºîºíºïðúóóºí÷¿ºðºóàCºñåËºòÜ©ö×ºõºôºöìÃéõºöã±äïàñ»¡ºüºúºøõúºþâ©ºùºýº÷ðÉéÎºýºûõ­ì²»¢ä°»£çú»¥»§Ùü»¤»¦á²âïìæìïóËìèð­à‚ð×»¨»ª»©æèîü»¬»«»¯»®»­»°èë»³»²»´»±õ×»µ»¶âµ»¹»·ä¡»¸ÝÈïÌ»·çÙ÷ß»º»ÃÛ¼»Â»½»»ä½»Á»¼»ÀåÕ»¾»¿»¼öéß§ëÁ»Ä»Å»Ê»ËÚò»Æáå»ÌäÒåØ»Íäêè«óò»Èñ¥»Ç»Éó¨öü»Ð»Î•s»Ñ»Ïœê»ÒÚ¶ßÔ»Ö»Óò³êÍçõ»Ô÷â»ÕãÄ»Øä§Üî»×»Ú»Ü»ã»á»äßÜ»á»æÜö»åí£»â»ßåç»Þ»àà¹»ÝçÀ»Ù»ÛÞ¥ó³»è»ç»éãÔ»ëâÆ»êÚ»»ì»ìñëïÁØå»íß«»î»ð»ï»ï»ï»ò»õ»ñ»ö»ó»ô»ñÚÊžmÞ½ó¶ÐÐí¹È¦°n¹k¼Õ‘Ø¢¼¥»÷ß´¼¢ØÀ»ø»úçá¼¡Ü¸í¶¼¦¼ª¼£ØÞßó¼§åì»ýóÇ»ù¼¨ïúê÷¼©êå»ûõÒ»þçÜ»üì´ÛÔ¼¤î¿¼°¼ªá§¼³¼¶¼´¼«Ø½Ù¥Š ¼±óÅ¼²ê«¼¬éê¼¯¼µé®Ýð¼­ñ¤Þª¼®¼¸¼ºò±¼·¼¹ÒÎêª¼¹÷ä¼Æ¼Ç¼¿¼Í¼Ë¼É¼¼ÜÁ¼Ê¼Á¼¾¼·¼ÈÔÛ¼Ã¼ÌêéÙÊ¼Å¼Ä¼Â¼À¼»ôßõÕö«öÝð¢öê¼½÷Ùæ÷¼Ó¼Ð¼ÑåÈ¼Ïä¤çì¼ÒðèóÕôÂÏ¼õÊ¼ÎïØáµÛ£¼Ôí¢ê©îòòÌ¼Õ¼×ëÎ¼Ö¼Øðý¼Û¼Ý¼Ü¼Ù¼Þ¼Úê§¼é¼âŽÔ¼á¼ß¼ä¼ç¼è¼æ¼à¼ã¹×Ç°êù¼êÞö¼åçÌÝóöäðÏº]žh÷µàî¼ðèÅ¼ó¼í¼ë¼ìóÈ¼õ¼ô¼ìõÂíú¼ïñÐïµ¼òÚÙê¯¼îôååÀå¿¼û¼þ½¨½¤½£êð¼ö¼ú½¡½§½¢½¥ÚÉ¼üë¦¼úëì¼ù¼ø¼üÙÔ¼ý½¨½­½ª½«½­½¬ôø½©çÖíä½®½²½±½¬½¯ñð½³½µä®ÌÐ½´êñôÝÜ´½»½¼æ¯½¾½½½»½¾½º½·½¹òÔõÓÙÕöÞ½¶½¸ðÔç€½ÇÙ®ÞØ½Æ½Ê½Èð¨½Ã½Å½Â½Á½Ëë¸½Ä½É½Ð½Î½Ï½Ì½Ñ½ÍàÝõ´½×ðÜ½Ô½Ó½ÕÐ³àµ½Ò½ÖæÝ½ÚÚ¦½Ù½ÜÚµÞ×½à½áèîæ¼½Ýò¡½Þ½ØíÙ½ßöÚôÉ½ã½â½é½ä½æ½ì½ç½ê½ë½èò»÷º½å½í½ñ½ï½ð½òñæñÆ½ø½î½ó½öÚá½ôÝÀ½÷½õâÛâËéÈèª¾¡¾¢½ñ½ü½øÝ£½ú½þ½ýêáçÆ½û½ùêî½ó¾©ãþ¾­¾¥¾£¾ªìºÝ¼¾§ëæ¾¦¾¬¾¤¾«¾¨¾®ÚåØÙëÂ¾±¾°ÙÓ¾°¾¯œQåò¾¶åÉëÖ¾·¾ºæº¾¹¾´¾¸¾³¾¹¾²¾µìçåÄ¾¼¾½¾À¾¿ð¯ôñãÎ¾¾¾¾÷Ý¾Å¾Ã¾Ä¾Á¾Â¾Æ¾É¾Ê¾Ì¾ÎèÑèê¾Ç¾È¾Í¾Ë¾ÍðÕ¾Ó¾Ð¾ÑÇÒ¾Ô¾ÒÞäé§¾Ýï¸ñÕöÂ¾Ï÷¶¾Ö½Û¾ÕéÙ¾×¾Ú¾Ù¾ØÜìé·é°ö´õá¾ä¾ÞÚª¾Þ¾ÜÜÄ¾ß¾æîÒ¾ãÙÆ¾Ó¾å¾Ý¾àêøì«¾âñÀ¾Ûåð¾áåáõ¶¾ê¾è¾è¾éïÔîÃ¾íïÃ¾ëèðáú¾îöÁ¾ìÛ²¾ï¾ïæÞ¾ñ¾÷¾ñ«içå¾ø¾õ¾ó¾ò¾òèöõûØÊØãÚÜâ±Þ§éÓ¾ôïãõê½ÀÛÇìß¾ð¾ü¾ý¾ù¾ûñä¾úóÞ÷å¿¡¿¤¾þÞÜ¿£¿¥¬B¿¢Ð®òÂ¼÷ßÇ¿§¿¦¿¨ØûëÌ¿ª¿«ï´¿­¿­Ûîâýîø¿®ÝÜ¿¬ïÇâé¿¯¿±íè¿°ê¬¿²Ù©¿³Ý¨¿´ãÛî«¿µ¿¶¿·÷K¿¸¿ºØøß’¿º¿¹ãÊ¿»îÖåê¿¼¿½¿¼¿¾îíêû¿¿¿À¿Á¿Âçæ¿ÆéððâîÝ¿Ãò¤ïýñ½¿Åî§¿Äòò÷Á¿Ç¿È¿É¿Á¿Ê¿Ë¿Ì¿Íã¡¿Îë´æìç¼¿È¿Äï¾¿Ï¿Ñ¿Ò¿ÐñÌ¿Ô¿Óï¬¿ÕÙÅáÇóí¿×¿Ö¿Ø¿Ù¿×íî¿Úßµ¿Û¿ÜóØÞ¢ØÚ¿Ý¿ÞÜ¥¿ß÷¼¿à¿âç«à·¿ã¿á¿äÙ¨¿å¿æ¿è¿çØá¿é¿ì¿ëÛ¦ßàáöëÚ¿ê¿í÷Å—p¿î¿ïÚ²ßÑ¿ð¿ñÚ¿ÞÅÚ÷ÛÛ¿ó¿ö¿õ¿óêÜ¿ò¿ô¿÷¿ùã¦¿ø¿ú¿üåÓØ¸¿üÞñ¿ûêÒ¿ýî¥òñÙç¿þõÍØÑà°ã´À¢À¡ÝÞÀ¡¹ñóññùÀ¤À¥Ÿjçûï¿÷Õõ«öïã§À¦ãÍ‰×—yÀ§À©À¨èéòÒÀ«Àªà~ö¦“š˜ÍÀ¬À­À²Áååê¹íÇÀ®´ÌÀ¯ðøÀ¯À±À´áÁáâäµÀ³ïªêãíùÀµäþñ®ô¥À¼á°À¹À¸À·À»À¶À¾À½ñÜìµÀºïçÀÀÀ¿ÀÂé­ÀÄî½ÀÁÀÃÀÄà¥ÀÉÀÇÝ¹ÀÈÀÅÀÆïüï¶òëÀÊãÏÀËÝõÀÌÀÍÀÎßëÀÌðìï©õ²ÀÏÀÐÀÑèáîîÁÊÀÔÀÓñìÀÒ‹ªØìÀÖÀß«WãîÀÕ÷¦À×æÐçÐÀ×ÀØÙúñçÚ³ÀÝ‚ñÀÚÀÙÀÜÀßÀáÀàÀÛõªÀÞÀÕÜ¨ÀâÀãÀäÀãÀåÀæÀêÀëÀòæêÀçà¬ Àð¿ÀìçÊÀëòÛæËÁ§öâÀèÀéî¾Þ¼÷óó»ÀñÀîÀïÙµÁ¨æ²åÎÀíï®Àðå¢õ·÷¯Á¦•ÑÀ÷Á¢ÀôÀöÀûÀøÀúÛÞÁ¤ÜÂÀýìåèÀÀ÷Á¥ÀþÙ³èÝðßÀóéöÛªÀõáû«†íÂÀùÎ»à¦óÒÁ£ôÏòÃÀüÁ¡îºÔ¾ö¨äàóöÁ½ÞÆÁ¬Á±Á¯Á¬Á«ÁªñÍÁ®öãå¥ì¡Á­ó¹Á²çöÁ³ñÏÝüÁ·æ®Á¶ÁµéçÁ´é¬Á²Á¼Á¹ÁºÁ¹é£Á¿Á»Ü®õÔÁ½÷ËÁÁÁÂÁ¾ÁÀÁ¿ÁÉÁÆÁÄÁÅÁÈÁÎàÚå¼ÁÃÁÅçÔÁÇÁÍðÓîÉÞ¤ÁËÞÍÁÏÁÌßÖÁÐÁÓÙýä£ÛøÁÒÞæÁÔÁÑôóõñ÷àÁÚÁÖÁÙÁÕÁÜÁÕôÔá×åàê¥ÁØî¬Á×çlÁÛò•÷ëÁÝâÞãÁéÝÁßÁÞÝþì¢õïÁàÁæÁéàòÁëÁîÜßÁàÁáê²ÁèÁåÁêèùÁè¬Oç±ÁçôáñöÁâòÈÁãÁäöìÛ¹ÁìÁîÁíßÊÁïÁïÁõä¯Á÷ÁôÁðÁòì¼åÞÁóæòÁñÁöïÖöÌÁøç¸ï³ÁùðÒÁúÁüÂ¢ÁüèÐççëÊíÃÁýÁûÂ¡ñªÁþÂ¤Â¢Â¢Â£Â¦ÙÍà¶ÝäÂ¥ñïò÷÷ÃÂ§Â§Â¨ÂªÂ©ðüïÎÂ¶Â³ß£Â¬Â«Â«ÛäãòÂ¯èÓëÍéñðµôµÂ­öÔ­ˆÂ±Â²Â°Â³éÖïå®fÁùÂÉˆvÂ¼Â¸éûäËåÖÂ¹¬fÂ»ÂµÂ·äõÂ¾ê¤Âºè´óüðØÂ´çeëªÂ¿ãÌÂÀÂÂÂÃÂÆÂÄÂÉÂÇÂÊÂÌÂÏÂÍÂÎèïð½ÙõÂÐöÇÂÑÂÒÂÓÂÔï²ÂÕÂØÂ×ÂÕÂÙÂÚÂÖÂÛÞÛÂßâ¤ëáÂÜÂßé¡ÂàÂáÂâïÝÂÝÙÀÂãñ§ÙùÀÖÂåÀÒÜýÂæçóÂäÞûäðöÃÆƒºÑÂèæÖÂéó¡ÂíÂêÂêÂëÂìè¿µlÂîßé‚ØôKÂðÂïÂñö²ÂòÂòÛ½ÂõÂóÂôÂöò©ÂùÂø˜ÑÂ÷÷´÷©ÂúòýÂüÃ¡Ü¬á£ÂýÂþçÏÂûì×ïÜÚøÃ¦Ã¢Ã¤Ã£íËÃ§Ã§òþÃ¨Ã«Ã¬êóÃ©ì¸òÖÃª÷Öòúó±Ã®á¹ã÷Ã®êÄÃ­Ã¯Ã°Ã³ë£ÙóÃ±à|è£î¦Ã²í®Ã´Ã»Ã¶ÃµÃ¼Ý®Ã·Ã½áÒäØâ­é¹ÃºÃ¸ïÑðÌÃ¹Ã¿ÃÀä¼œ„Ã¾ÃÃÃÁÃÄÃÂ÷ÈÃÅÞÑîÍÃÆìËí¯ÃÇÃ¥òµÃÈÃËÝùÞ«ëüÃÊíæô¿ÃÍÃÍÃÉÃÌô»òìãÂó·ÃÏÃÎßäÃÖìòÃÔâ¨ÃÕÃÑÃÓ÷ã÷çÃÒÞÂÃ×ØÂåôÃÔôÍëßÃÐæùÀáåµÃÚÃÙÃØÃÜÃÝÚ×à×ÃÛÆPÃßÃàÃÞÃâãæö¼ÃãííÃäÃáäÏÃåëïÃæß÷ÃçÃèÃéðÅèÂíðÃëÃìÃìç¿ÃêÃ²ÃîÃíÑòÃðÃïÃïóºÃñáº•FçäÜåçëçÅÃóãÉÃòãýÃöÃõÃôíªíª÷ªÃûÃ÷ÃùÜøÚ¤ÃúàpäéêÔî¨Ãøõ¤ÃüÃýçÑÃþÚÓæÆâÉÄ¡Ä£Ä¤Ã´Ä¦Ä¥Ä¢Ä§Ä¨Ä©éâÄ­ÜÔÄ°ï÷ÄªÄ¯Ä®ÝëõöÄ«ñ¢ïÒÄ¬õøñòÄ²Ä²Ù°íøÄ±öÊÄ³Ä¸ë¤Ä¶ÄµÄ·Ä´Ä¾ØïÄ¿ãåÛéÄÁÜÙîâÄ¼Ä¹Ä»ÄÀÄ½ÄºÄÂäÅÞÖ¶ùÂYØ¿ÄÃïÕÄÄÄÚÄÇÄÉëÇÄÈñÄÄÆÞàÄËÄÌÄÌÄÊÄÎÄÎÄÍÝÁØ¾Å®ÄÐ‚OÄÏÄÑà«ÄÏéªôö“Dœ¯ëîòï‹RàìÄÒâÎêÙß­Ø«ßÎÄÓâ®òÍÛñÄÕÄÔè§ÄÖ×¿Ä×Ú«ÄÅÄØÄÙÄÛÄÜÄÝÄáÛèâõÄàÄßîêâ¥ÄÞöòÙ£ÄãÄâì»êÇÄæÄäÄçíþÄåÄéÄêöÓöóð¤ÄíéýÄìÄëØ¥ÄîµæÄïÄðÄñÄñôÁæÕÄòëåÄóÚíÄù¶üô«ýmÄôÄ÷Äøò¨õæÄõÞÁÄúÄþßÌÅ¡ÄüÄûñ÷ÄýØúÅ¢ÄþÅ¥Å£³óÅ¤Å¦Å¦–ƒÅ¥Å©Ù¯Å§Å¨Å§Åª˜‰ññÅ«æÛæåÅ¬åóæÀÅ­Å®îÏÄÍô¬Å°Å°Å¯Å²ÙÐÅµÅµÞùï»Å³Å´í¥ÄèàÞÅ¶Ú©Å·Å¹Å·Å¸Å»Å¼ÅºÅºâæÅ½îÙÅö—”¬i½læq±Ùí@ÆªÆ´ÌÂíûË‘·…±êñFùL…Ä·Â¸¬Œ´°ÅÅ¿Å¾ÝâèËÅÀ°ÒÅÃóáÅÁÅÂÅÄÙ½ÅÇÅÅÅÆ¹uÅÉÅÉÅÈÝå±eÅËÅÊÆ¬°éÅÌÅÍõçó´ˆmÅÐãúÅÑÅÎÅÏñÈÅÊÅÒäèÅÓåÌÅÔó¦ÅÕÅÖÅ×ëãÅÙÅØâÒáóÅÚÅÛÞËÅÜÅÝðåÅÞÅßõ¬ÅãÅàÅâïÂÅáÅæÅåàúì·«˜ÅäàÎö¬ÅçÅèÅèâñÅêÅéÅëàØÆMÅó‚‡ÅïÅíÅï‚õ“sÅðÅîÅôÅìÅñÅòó²Åõ„™ÅöØ§ÉÅúÚüÅ÷ÅûÅøîëÅüÅùÆ¤ÜÅèÁÅþÆ£ò·Û¯ÚðÆ¡Ûý—ÀÅýÆ¢î¼òçõùÜ±Æ¥±ÈÛÜÆ¦ñ±Æ¨æÇî¢Æ§Æ©Æ¬Æ«êúÆªôææéëÝõäÚÒÆ­ØâÆ¯çÎÆ®óªÆ°éèî©Æ±Æ±æÎë­Æ²Æ³ÜÖæ°Æ´Æ¶æÉÆµò­Æ·é¯êòÆ¸Æ¸Æ¹Ù·Æ½ÆÀÆ¾ÆºÆ»àZÆÁèÒ›¯«rÆ¿Æ¼öÒîÇÆÂ·¢ÆÄÆÅÛ¶ð«ØÏîÞóÍÆÈçê†\ÆÆÆÉÆÇÆÊÅàÙöÆÍê·ÆËÆÌàÛõ‹ÆÏÆÎÆÐÆÏÆÑè±å§ïäÆÓÆÔÆÒÆÖÆÕäßÆ×ë«ïèõëÆØû]Ç×¬gñÊ½®äÐá½ôòöÄÆßÆãÆÞÆâÆàÆÜèçÆÝÝÂÆÚÆÛÆÝéÊÆáõèØÁÆîÆëÛßáªÜÎÆäÆæÆçÆíêÈÆêñýÆéÆüÆèÝ½æëÆïÆåçùç÷ì÷òÓÆìôëòàÐ½÷¢÷èÆòÆóá¨ÆñÜ»†™è½Æðç²ôìÆøÆýãàÆùÆúÆûÆüÆõÆöÜùÝÝíÓÆ÷í¬ÆþÝÖÇ¡Ç¢÷ÄÇ§ÇªÚäÇ¤Ü·Ç¨ÙÝá©âTÇ£ã¥Ç¦Ç«í©Ç©å¹åºå½Ç°îÔò¯Ç®Ç¯Ç¬ÞçóéÇ±Ç­Ç³ëÉã»Ç²Ç´ç×Ç·Ç·ÜçÙ»ÇµÇ¶èýÇ¸ÇºÇ¼ãÞê¨Ç¹õÄÇ»òÞïºïÏïêÇ¿ Ç½Ç¾éÉÇÀôÇñßìÁÇÄíÍõÎàƒØäÇÃÇÂÇÁçØÜFÇÅÇÈÜñÇÅÚÛã¾÷³éÔÇÆÇÉã¸ÇÎÚ½ÇÍÇÏÇÌÇËÇÊÇÐÇÑÇÒæªÇÓÇÔêüã«óæïÆÇ×ÇÖÇÕôÀÂÜËÇÛÇØÇÙÇÝÇÚàºäÚÇÝÇÜÇÜòû½þÇÞßÄÇßÞìÇàÇâÇáÇãÇäÇàÇåòßöëÇéÇçÇèÇæ¾¯÷ôÜÜÇêÇëö¥ÇìóäíàóÀõ¼öÆÚöÇîñ·ÜäóÌÇíòËÇðÇñÇïòÇÈcé±öúÇôáìÇóò°ÇöÙ´ÇõÞåÏÇòêäÛÏåÙôÃòø÷üôÜÇøÇúÇøÚ°ÇýÇüìîÇùÇûòÐÇ÷ôðôð÷ñÛ¾ëÔð¶ÇþÞ¡íáè³Þ¾ë¬ñ³áéó½È¡È¢È£È¥ãÖêïÈ¤ãªÈ¦çzÈ«È¨Ú¹ÈªÜõÈ­éúÈ¬îýóÜòéÈ©÷ÜÈ§È®î°ç¹È°È¯È²È±È³È´í¨È¸È·ã×ãÚÈµÈ¶ËóÈ¹ÈºÛ§òÅÈ»÷×È¼È½ÜÛÈ¾žìüÈ¿ð¦ÈÂÈÀÈÁÈÃÜéÈÄèãÈÅæ¬ÈÆÈÇÈÈÈËÈÊÈÉÈÌÜóïþÈÐÈÏØð×šÈÎÈÒÈÑéíÈÍâ¿ñÅÈÓÈÔµiÈÕÈÖëÀáõÈÞÈ×ÈÙÈÝáÉÈÜÈØéÅÈÛòîéFÈÚÈÚÈßÈáÈàôÛõå÷·ÈâÈçÈãï¨ÈåàéÈæÈåÞ¸ñàÈäò¬ÈêÈéÈèàrÈëÈçäáçÈÝêÈìÈîÈîÈíÞ¨ÈïÜÇèÄò¸ÈñÈðî£ÈòÈòÈôÙ¼ÈõàeÈôœcóèè¼Ë¼·_²Ù²ô¾D³×ÝçiÝØØíÈöÈ÷Ø¦ìªëÛÈøÈûÈùÈûÈúÈüÈýÈýë§É¡É¢ôÖâÌÉ£É¤ÞúíßòªÉ¥É¦É§çÒëýöþÉ¨É©Ü£ðþÉ«É¬ØÄï¤¬XÉªð£É­É®É±É³É´É°É¯ï¡ððôÄöèÉµßþÉ¶¿³É·ö®É¸É¹É½ÈýÉ¾É¼ÜÏæ©ÉÀîÌÛïÉºô®õÇÉ¿äúëþÉÁÉÂÚ¨ÉÇðÞÉ»ÉÈÉÆæóÛ·ÉÉæÓÉÃÉÅÉÄóµ÷­ÉËéäÉÌõüÉÊìØÉÑÛðÉÎÉÍÉÏÉÐç´ÉÓÉÒÉÕÉÔóâô¹òÙÉ×ÉÖÉØÉÙÛ¿ÉÛÉÛÉÜÉÚäûÉÝâ¦ÉÞî´ÉàÙÜÉßÉáØÇÉèÉçÉäÉæÉâÉåÉãäÜ÷êÉêÉìÉíêÉëÉðÚ·«|ÉïÉéÉîÉñ³ÁÉóßÓïòÚÅÉôäÉÉöÉõëÏÉøÉ÷é©ò×ÉýÉúÉùÉüÊ¤óÏÉûÉþÊ¡íòÊ¥êÉÊ¢Ê£áÓÊºÊ§Ê¦Ê«ßŸÊ©Ê¨Êªõ§Ê®Ê²Ê¯Ê±Ê¶ÊµÊ°Ê´Ê³Ê·Ê¸Ê¹Ê¼Ê»Ê¿ÊÏÊÀÊËÊÐÊ¾Ê½ÊÂÊÌÊÆÊÓÊÔÊÎÊÒÊÇÊÁÕâéøÊÅÒæÊÍóßÊÄŠ]ÊÕÊÖÊØÊ×ô¼ÊÙÊÜá÷ÊÞÊÛÊÚç·ÊÝÊéì¯Êãç£ÊåÊàêxÊâÙ¿ÊâÊáÊçÝÄàgÊèÊæÞóë¨ÊäÊßïøÊëÊêÛÓÊì­qÊîÊòÊðÊóÊñÊíÊïÊõÊùÊøÊöÊöÊ÷ÊúË¡ÊüÊýëòÊûÊþäøË¢Ë¢Ë£Ë¥Ë¤Ë¦Ë§ó°ãÅË©Ë¨äÌË«Ëªæ×Ë¬Ë­Ë®Ë°Ë¯Ë±Ë³Ë´Ë²ËµåùË¸Ë·îåË¶àÊÞ÷ÝôËÔéÃÛÌË¿Ë¾Ë½ßÐË¼ð¸Ë¹çÁòÏïÈË»ËºäùËÀËÈËÄËÂ¼ÇËÅËÆÂÂÙîËÆìëãôËÇæáÙ¹óÓñêËÃËÁòIâìËÉŠ»Ú¡áÂäÁËÉáÔËËã¤ËÊñµËÏËÎËÐËÍËÌà²ËÑÉ©âÈì¬ïËËÒòôÛÅàÕî¤ËÓÊýËÕËÖöÕË×«TÙíËßËàä³ËØËÙËÚÚÕà¼ËÜãºËÝËÛÝøö¢­Xóùâ¡ËáËâËãËäÝ´íõËùî¡å¡ËçËåËæËèËêËîÚÇËìËéËíìÝËëåäËïáøÝ¥â¸ËðËñöÀé¾Ëôæ¶êýèøËóíüàÂôÈËòËõËùßïË÷ËöËöÏÃÝ·ñâÕ¤Êô²_³Êk‚mÌ¤Ì­ìâµ÷î×¶ÚËýÜæËûËüõÁîèËúäâËþÌ¡÷£Ì¢ãËåÝé½Ì¤Ì£ææÌ¥Ì¨Û¢Ì§Ì¦ìÆõÌöØÞ·Ì«Ì­Ì¬ëÄîÑÌ©ÌªÌ®Ì°Ì¯Ì²Ì±Ì³ê¼Ì¸Û°ñûÌµïÄÌ·Ì¶Ì´ìþÌ¹Ì»Ì¹ÌºšUÌ¿Ì½Ì¼ÌÀï¦ôÊïÛÌÀÌÆÌÃÌÄÌÁÌÂÌÁÌÆéÌÌÅÌÇó¥ó«õ±àûÌÈÌÊÙÎñíÌÉÌÌÌË|ÌÎ¿lÌÍþÌÏèº÷ÒÌÒÌÓÌÒÌÕßû—ƒÌÔÌÑ™„Ø»ÌÖÌ×ìýß¯ÌØï«í«ÌÛÌÚÌÜëøÌÙÌÞÌÝÌàÌßç°ÌÝÌäÌáç¾ðÃzÌâÌãõ®ÌåÌëÌêÙÃã©ÌéåÑÌèÌæñÓÌçÌìÌíÌïÌñî±ÌðÌîãÙãÃéåœL¬_ÌóÌòÞÝÙ¬ÌôìöÌõÌöóÔö¶µñ÷Øöæñ»Ì÷ôÐÌøÌùÌùÌúÌû÷ÑÌüÍ¡ÌýÌþÍ¢Í¤Í¥ÜðÍ£æÃÝãòÑöªÍ¦èè¬EÍ§Í¨Í¨ÙÚÍ¬Ù¡Í®Í²Í©íÅÍ­Í¯ÍªÍ¯äüÍ«Í³Í±Í°Í²âúÍ´ÍµÍ·Í¶¹ÉÍ¸Í¹ÍºÍ»Í¼Í¿Í½Í¿Ý±Í¾ÍÀõ©ÍÁÍÂîÊÍÃÜ¢ÝËÍÄÍÅ×¨î¶åèÍÆÍÇÍÈÍËìÕÍÉÍÊÍÌêÕÍÍâ½ëàÍÎÙÛØ±ÍÐÍÏÍÑÍÔÙ¢ÍÓÛçãûÍÕèÞíÈÍÒõÉõ¢éÒö¾Í×âÕÍÖÍØèØÍÙóêà„ÍÛÍÞÍÚÍÝæ´ÍÜÍßØôÍàëðÍááËÍâÍäØàÍåòêÍãÍèæýÜ¹ÍêÍæÍçÍéÍðÍìÍíÍñÍïçºëäÝÒçþÍîîµÍëÍòÍóÍôÍöÍõÍøÍùÍ÷Øèã¯éþ÷ÍÍýÍüÍúÍûÎ£ÍþÙËÎ¯ÚñÝÚÎ¢ìÐÞ±Î¡ ‘Î¤Î§àøÎ±Î¥ãÇÎ¦ä¶Î¨á¡Î©Î¬áÍœ‘Î«Î°ƒ^Î²Î³Î­Î¯ì¿çâÓÐæ¸ÚÃÎ®À¢â«ÉJðôôºè¸öÛÎÀÎ´Î»Î¶Î·Î¸ê¦Î¾Î½Î¹Î¼â¬ÎµÎ¿ÎºÎÂÎÁÎÄÎÆÎÅÎÃãÓö©ØØÎÇÎÉÎÈÎÊãëè·ÎÌÎËÝîÎÍÞ³ÎÎÙÁÎÐÝ«à¸ÎÏÎÏÎÒÎÖë¿ÎÔÎÝá¢ÎÕä×íÒÎÓö»ÎÚÛØº¹ÚùÎØÎ×ÎÝÎÜÎÙÎÞÎãÎâÎáÎßÎàä´òÚ÷ùÎåÎçØõÎéÎëåüâÐâèâäåÃÎäÎêÎæêõ« ðÄÎèØ£ÎðÎñÎìÚã’Nè»ÜÌÎïÎóÎòÎîìÉæÄðíæðÎíå»ðÍöÈšGÎüœä¸ßàAÓiÛ¨Ï¾¼ÙàåáÆÜÝ¡À]ÏÈËÞ’Û×Ï¦ÙâÏ«Î÷ÎüÏ£ÎôÎö¹èñ¶Û­Ï£ÞÉŠÖÏ¢•„Ï¡ÎþÏ¤Ï§ì¤ÕãÏ©ÎøÕÛ‚ÝÎúÏ¬Ï¡ôÑôâô¸ÏªðªÎýÙÒÏ¨ÎõòáÎûæÒÏ¥éØì¨ìäôËó£ Oó¬ØGëvõµêØ÷ûÏ°Ï¯Ï®êêÏ±ÚôÏ­Ï´çôÍ½Ï³Ï²ÝßåïÝûÏ²÷^Ï·Ïµâ¾Ï¸Û§ãÒôªÏ¶ìùÏºÏ¹Ï»ÏÀáòÏ¿èÔÏÁíÌåÚÏ¾è¦Ï½Ï¼÷ïÏÂÏÅÏÄóÁÏÉÏÈÏËë¯ììôÌÝ²ÏÆõÑõ£ÏÆÏÊåßÁÏÐÏÒÏÍÏÌÏÑÏÐÏÏÏÎðïðÂÏÓÏ´ÏÔÏÕáýò¹óÚõÐÞºìÞÏØá­ÜÈÏÖÏßÏÞÏÜÏÝÏÚÀ‰ÏÛÏ×ÏÙÏßö±ÏçÏçÏàÏãÏáÏæç½ÏäÏäÏåæøÏâÏêâÔÏéÏèÏíÏìâÃ÷ÏÏëößÏòÏïÏîÏóÏñÏðó­èÉÏ÷ÏþèÕæçÏüÏûç¯åÐÏôÏõÏúäìóïÏö÷ÌÏùáÅÏýÐ¡ÏþóãÐ¢Ð¤ÏøÐ§Ð£Ð¦Ð¥Ð©Ð¨ÐªÐ«Ð­Ð°Ð²ÙÉÐ±Ð³Ð¯ÛÄß¢çÓÐ¬Ð´Ð¹ÐºÐ¹Ð¶Ð¼‚ÄÐµÙôµúÐ»éÇé¿âÝÐ¸â³Þ¯åâÛÆå¬Ð·õóÐÄß”ÐÃÐ¾ÐÁê¿ÐÀÐ¿ÐÂì§Ð½Ü°öÎØ¶ÐÅÐÆÐËÐÇÐÊÐÉÐÈÐÌÐÏÐÎÚêÐÍíÊÐÑß©ÐÓÐÕÐÒÐÔÜôŠüÐÔ›ëÐ×ÐÖÐÙÜº×›ÐØÐØÔKÐÛÐÜÐÝÐÞÐÝâÓÐßð¼õ÷âÊ÷ÛÐàÐãá¶ÐåÐäÐâçn³ôÐçíìÐöÐëÓ’çïÐéšHÐêÐèÐæÐìÐìÐíÚ¼èò«ôÚÐñÐòÐðÐôÑªÛÃÐ÷ÐøÐïÐöÐðÐõÐáìãÐîÞ£ÐùÐûÚÎÐúÞïÝæêÑìÓ²UÙØÐþ«tðçÐüÐýäöè¯Ñ¡Ÿ@Ñ¢ãùìÅÑ¤Ñ£îç¬KäÖé¸íÛïàÑ¥Ñ¦Ñ¨Ñ§í´õ½Ñ©÷¨ÑªÚÊ„ìÛ÷Ñ¬ñ¿â´Þ¹êÖõ¸Ñ°Ñ²Ñ®Ñ±Ñ¯á¾âþÑ®Ñ°Ñ±Ü÷«‘Ñ­à‰öàÑµÑ¶Ñ´Ñ¸áßÑ·Ñ³ÙãÞ¦Óõ…¥‹jéœ••‰¥Ó”½Ä‚\Šxç~åWØß›âÍë¬^àN›ªÔDÃ‘Ñ¾Ñ¹Ñ½ÑºÑ»èâÑ¼ÑÀØóá¬Ñ¿…ƒçðÑÁÑÂÑÄíýÑÃÑÆðéÑÅÑÇÑÈåÂÛëæ«í¼ë²ÞëÑÊâûÑÌëÙáÃÑÍÑÉÓÙÑËÑÝëçƒBÛ³æÌÑÓãÆÑÏÑÐÜ¾ÑÔÑÒÑØÑ×ÑÐÑÎÑÖóÛÑÑÑÕéÜÙðÑÙÙ²ÑçÑÜÙÈØÉÑÚÑÛçüî»ÑÝ÷Ê÷úÑáÑåÑâÑäÑçêÌØWÑéÑèÑßÑæ»ðÑãÑÞõ¦ÚÝÑàØÍÑàÑëãóÑêÑíÑì÷±ÑïÑòÑô•DÑîì¾ÑðÑñáàÑóìÈ¤òÕ•ªÑöÑöÑøÑõÑ÷âóí¦ÑóÑúçÛØ²Ò§ÑýÑüÑûØ³Ò¢ëÈÒ¦é÷çòÒ¤‚çÒ¥áæÒ¡Ò£Ñþôí÷¥èÃÒ§ñºÒ¨ÑüÒ©ÒªðÎê×Ò«Ò¬Ò­Ò¯Ò®ÞÞîôÒ²Ò±Ò°ÒµÒ¶Ò·Ò³ÚþÒ¹êÊìÇÒ´ÒºÚËÒ¸ØÌÒ»ÒÁÒÂÒ½ÒÀµtßÞâ¢Ò¿Ò»Ò¾ì¥äôàæ÷ðÒÇÛÝÒÄÒÊÚ±ÒËâùåÆâÂß×ÒÌÜèêÝíôÒÈôýðêÒÆÚ±ÒÅÒÃÒÉáÚÒÍÒÒÒÑÒÔîÆÌÒÓÜÓô¯ÒÏÒÐÒÎì½ÒåÒÚß®Ø×ÒäÒÕØîÒéÒàÒÙÒìØýß½ÒÛÒÖÒëÒØÇÄá»âøÒ×ÒïÒèæäÞÈÞÄÒßôàéóã¨ÞÚÒæÒêÛüñ´ÒîÒÝÒâÒççËÒÞÒáðùòæÒãìÚï×Øæéì DÞ²ôèÒíÒÜñ¯ïîÀXž‹Ü²ÒòÒõÒöÒòÒðÒñÒôÒóë³î÷à³Ü§Ò÷Ûóáþ«ÒúÒùÒøÛ´Òúö¸ö¯ÒüÒýÒýÒûò¾Òþñ«Ó¡Ó¡Ø·Ó¦Ó¢ÝºÓ¤Ó¢çøàÓÞüÓ§ó¿Ó£Ó£ðÐâßÓ¥Ó­ÜãÓ¯ÜþÓ«Ó¨Ó©ÓªÝÓéºäÞÝöäëÓ¬ÙøÓ®å­Û«ò£Ó±Ó°ñ¨Ó³Ó²ëôÓ´ÓýÓ¶ÓµÓ¸çßÓ¹à{ÓºÜ­ã¼ÛÕÓ·÷«÷ÓÓöÓÀð®Ó½Ó¾Ù¸ÓÂÓÂÓÁÓ¼Ó»ÓÃÓÅÓÇØüßÏÓÄÓÆÞÌÓÈÓÉÓÌÓÊÓÍèÖðàÝ¯ÝµÓËòÄÓÎöÏéàòöÓÑÓÐØÕÓÏÁhÐãîðë»÷îÓÖÓÒÓ×ÓÓÙ§àóå¶ÓÒÓÕòÊÓÔ÷øÓÚÓØÓÙÓåðöÓÚÓèßŽÓàæ¥ì£ÓëÓÚÓÛô§ÓãÓáØ®óÄô¨ÓéáüÚÄâÅÓãÝÇÓçö§áÎÓäÓáëéÓâÓÞÓÜè¤ÓÝêìñ¾ÓßòõÓëØñÓîÓìÓðÓêÙ¶ÓíÓïÎáÐÒâ×àhÍòðõñÁö¹ÓñÔ¦í²ÓóåýâÀÓýÓôªêÅÓüÓøÔ¡îÚÔ¤ÓòÓûœUÚÍãÐÓ÷Ô¢Óù¬ZÔ£ÓöðÁÓúìÏÝ÷ÓþØ¹òâÔ¥ìÛðÖå÷ð°Ô©íóÔ§Ô¨óîÔªÔ±Ô±ÔªÔ«ë¼Ô­Ô²Ô¬Ô®Ôµö½Ü«Ô´Ô³Ô¯éÚó¢Ô¶Ô·Ô¹ÔºæÂÔ®Ô¸Ô»Ô¼ÔÂë¾µjÔÀÔ¿ÔÃîáÔÄÔ¾ÔÁÔ½Ô½Ùßå®ügÔÆÔÈŠu›Vç¡Ü¿êÀÔÇÔÅëµÔÊÔÊÔÉÔÉÔÐÔËÛ©ã¢ÔÎÔÍã³Àˆè¹ÔÏìÙÔÌÔøÛ‚Æƒ]³¤ü…Ãq½‚¼—“oÇïódËF´ËÚõ¡™çà©ºd„–ÔÑÔÒÞÙÔÓÔÒÔÖçÞÔÕÔÔÇÔ×ÔØáÌÔÙÔÚôØô¢ÔÛêÃÔÜƒ­ôõÔÝÔÞöÉ­ÔÜÔßê°æàÞÊÔáÔáÔâÔãÔäÔçÔæÔéÔèÔåÔîÔíÔíÔìÔëÔïÔêÔòÔñÔóÔðåÅßõàýóÐô·óåØÓØÆê¾ÔôÔõÚÚÔöÔ÷çÕîÀï­êµÔùß¸Õ¦ßîÔûÞêÔü²é÷þ÷þÔúÔýÔþÕ¢Õ¡Õ£íÄÕ§Õ©ß¸Õ¨ðäòÆÕ¥Õ«ÕªÕ¬µÔÕ­Õ®íÎÕ¯ñ©Õ´Õ±ì¹Õ³Õ²ÚÞÕ°ø@Õ¶Õ¹ÕµÕ¸ÞøÕ·Õ¼×Õ½Õ»Õ¾ÕÀÕ¿ÕºÕÅÕÂÛµæÑÕÃÕÄâ¯ÕÁè°ó¯ØëÕÇÕÆÕÉÕÌÕÊÕÈÕÍÕÊÕÏÕÂá¤ÕÎîÈÕÐÕÑßúÕÒÕÓÕÙÕ×Ú¯ÕÔóÉ×ÀÕÕÕÖÕØÃDòØÕÚÕÛÕÜéüÕÝÚØß¡íÝÕÞÕßÕßô÷ñÞÕâèÏÕãÕáðÑÕêÕëÕìÕêÕäèåÕæÕèìõÕåÕçÇØé»óðÕéæPÕïÕíëÓéôî³ÕîçÇð¡ÛÚÕóð²ÕñëÞêâÕòÕðÕùÕ÷Õ÷Õúá¿ÕõÕøîÛÕöï£óÝÕôáçÕüÕûÕýÖ¤ÚºÖ£Ö¡ÕþÖ¢Ö®Ö§Ø´Ö­Ö¥Ö¨Ö¦ÖªÖ¯Ö«èÙìóëÕÖ¬ÖÁÖ©Ö´Ö¶Ö±ÖµÛúÖ°Ö²Ö³ôêõÅÞýõÜÖ¹Ö»Ö¼Ö·›bÖ½Ö¥ìíåëÖ¸Ö¯Ö°Öºíéõ¥ÖÁÖ¾ÆçõôÖÆàùÖÄÖÎÖËÖÊÛ¤ÖÅèÎÚìÖ¿•yèäÖÈÖÂêÞéùÖÀÖÌÖÏðºåéÖÇÖÍðëòÎæïÖÉÖÃÖÉëùö£õÙ­}ÖÐŠqÖÒÖÕÖÑïñô±ÖÔïñó®Ö×ÖÖÚ£õàÖÙÖÚÖØÖÝÖÛÖßÖÜÖÞàXÖàæ¨ÖáíØÖâÖãæûÖäÖæç§ÖçëÐÝ§ÖåôüÖèô¦ÖìÙªÖïÛ¥ä¨ÜïÖêÖéÖîÖíîùÖëéÆäóéÍÖñóÃÖòÖðô¶ðñõîÖ÷ÖôÁCÖîÖóÖö÷æÖõØù×¡ÖúÜÑÊã×¢Öü×¤ÖùÖ÷×£ðæÖøÖûÖþ×¢Öýóçôã×¥×¦×§×¨×©ò§×ª×ª×¬×«×­âÍ×±×¯×®×°×³×´´±×²‘Þö¿×·æí×µ×¶×¹×ºã·çÄ×¸ëÆñ¸×»»´×¿×¾Ù¾×½×À—‡äÃ¬k×Æ×Âí½×Ç×½ÚÂ×Ã×ÄÖø×Áìúßªåªïíž•×Ð×Î×È×É×Ë×Èêß×Ê×Íç»ÚÑæÜáÑ×Ìê¢×ìôôïÅö·÷Úöö×Ñ×Óæ¢ïöñèóÊè÷×Ï×Òö¤×Ö×Ô×É×Õíö×Ú×Û×Øëê×Ù×××ÜÙÌ×ÝôÕ×ÞæãÚÁµËÚîÛ¸öí×ß×à×á×âÝÏ×ã×ä×å‚úïß×ç×è×éÙÞ«~×æõòçÚ×ë×êß¬×ì×î×ïÞ©×í×ð×ñé×ç÷®ƒVß¤×ò×ó×ô×÷×øÚèÕ¨×õìñëÑ×ø×ù×ö»»";

    public static String fan2jian(String string) {
        return fan2jian(new StringBuffer(string));
    }

    /**
     * ½«´«ÈëµÄ·±Ìå×Ö·û´®×ª»»Îª¼òÌå
     * 
     * @param string
     * @return
     */
    public static String fan2jian(StringBuffer string) {
        int location;
        for (int i = 0; i < string.length(); i++) {
            location = -1;
            String temp = string.substring(i, i + 1);
            // byte[] bt = temp.getBytes();
            // if (bt.length > 1) {// Í¨¹ýµ±Ç°×Ö·ûµÄ×Ö½ÚÀ´ÅÐ¶Ï£¬Èç¹û´óÓÚÒ»¸ö×Ö½Ú¾ÍÈÏÎªÊÇÖÐÎÄÁË
            // if ((location = fanti.indexOf(temp)) >= 0) {
            // temp = jianti.substring(location, location + 1);
            // }
            // if (location > -1) {
            // string.replace(i, i + 1, temp);
            // }
            // }
            if ((location = fanti.indexOf(temp)) >= 0) {
                temp = jianti.substring(location, location + 1);
            }
            if (location > -1) {
                string.replace(i, i + 1, temp);
            }
        }
        return string.toString();
    }

    public static String jian2fan(String string) {
        return jian2fan(new StringBuffer(string));
    }

    /**
     * ½«´«ÈëµÄ¼òÌå×Ö·û´®×ª»»Îª·±Ìå
     * 
     * @param string
     * @return
     */
    public static String jian2fan(StringBuffer string) {
        int location;
        for (int i = 0; i < string.length(); i++) {
            location = -1;
            String temp = string.substring(i, i + 1);
            // byte[] bt = temp.getBytes();
            // if (bt.length > 1) {// Í¨¹ýµ±Ç°×Ö·ûµÄ×Ö½ÚÀ´ÅÐ¶Ï£¬Èç¹û´óÓÚÒ»¸ö×Ö½Ú¾ÍÈÏÎªÊÇÖÐÎÄÁË
            // if ((location = jianti.indexOf(temp)) >= 0) {
            // temp = fanti.substring(location, location + 1);
            // }
            // if (location > -1) {
            // string.replace(i, i + 1, temp);
            // }
            // }
            if ((location = jianti.indexOf(temp)) >= 0) {
                temp = fanti.substring(location, location + 1);
            }
            if (location > -1) {
                string.replace(i, i + 1, temp);
            }
        }
        return string.toString();
    }

    public static void main(String[] args) {
        String str = "Ä£·ÂÕZÑÔ°üÊ½µÄº†·±ÞD“Q¹¦ÄÜ²å¼þ£¬ÝpËÉŒ¬F·±ówºÍº†ów»¥“Q£¬ºÜ·½±ã£¬Ö»ÓÐÒ¼‚€JSÎÄ¼þ¡£ ";
        log4j.logDebug(str);
        str = fan2jian(new StringBuffer(str));
        log4j.logDebug(str);
        str = jian2fan(new StringBuffer(str));
        log4j.logDebug(str);
    }
}
