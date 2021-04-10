package org.spongycastle.math.ec;

import java.math.*;
import org.spongycastle.util.*;

class LongArray implements Cloneable
{
    private static final short[] INTERLEAVE2_TABLE;
    private static final int[] INTERLEAVE3_TABLE;
    private static final int[] INTERLEAVE4_TABLE;
    private static final int[] INTERLEAVE5_TABLE;
    private static final long[] INTERLEAVE7_TABLE;
    private static final String ZEROES = "0000000000000000000000000000000000000000000000000000000000000000";
    static final byte[] bitLengths;
    private long[] m_ints;
    
    static {
        INTERLEAVE2_TABLE = new short[] { 0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85, 256, 257, 260, 261, 272, 273, 276, 277, 320, 321, 324, 325, 336, 337, 340, 341, 1024, 1025, 1028, 1029, 1040, 1041, 1044, 1045, 1088, 1089, 1092, 1093, 1104, 1105, 1108, 1109, 1280, 1281, 1284, 1285, 1296, 1297, 1300, 1301, 1344, 1345, 1348, 1349, 1360, 1361, 1364, 1365, 4096, 4097, 4100, 4101, 4112, 4113, 4116, 4117, 4160, 4161, 4164, 4165, 4176, 4177, 4180, 4181, 4352, 4353, 4356, 4357, 4368, 4369, 4372, 4373, 4416, 4417, 4420, 4421, 4432, 4433, 4436, 4437, 5120, 5121, 5124, 5125, 5136, 5137, 5140, 5141, 5184, 5185, 5188, 5189, 5200, 5201, 5204, 5205, 5376, 5377, 5380, 5381, 5392, 5393, 5396, 5397, 5440, 5441, 5444, 5445, 5456, 5457, 5460, 5461, 16384, 16385, 16388, 16389, 16400, 16401, 16404, 16405, 16448, 16449, 16452, 16453, 16464, 16465, 16468, 16469, 16640, 16641, 16644, 16645, 16656, 16657, 16660, 16661, 16704, 16705, 16708, 16709, 16720, 16721, 16724, 16725, 17408, 17409, 17412, 17413, 17424, 17425, 17428, 17429, 17472, 17473, 17476, 17477, 17488, 17489, 17492, 17493, 17664, 17665, 17668, 17669, 17680, 17681, 17684, 17685, 17728, 17729, 17732, 17733, 17744, 17745, 17748, 17749, 20480, 20481, 20484, 20485, 20496, 20497, 20500, 20501, 20544, 20545, 20548, 20549, 20560, 20561, 20564, 20565, 20736, 20737, 20740, 20741, 20752, 20753, 20756, 20757, 20800, 20801, 20804, 20805, 20816, 20817, 20820, 20821, 21504, 21505, 21508, 21509, 21520, 21521, 21524, 21525, 21568, 21569, 21572, 21573, 21584, 21585, 21588, 21589, 21760, 21761, 21764, 21765, 21776, 21777, 21780, 21781, 21824, 21825, 21828, 21829, 21840, 21841, 21844, 21845 };
        INTERLEAVE3_TABLE = new int[] { 0, 1, 8, 9, 64, 65, 72, 73, 512, 513, 520, 521, 576, 577, 584, 585, 4096, 4097, 4104, 4105, 4160, 4161, 4168, 4169, 4608, 4609, 4616, 4617, 4672, 4673, 4680, 4681, 32768, 32769, 32776, 32777, 32832, 32833, 32840, 32841, 33280, 33281, 33288, 33289, 33344, 33345, 33352, 33353, 36864, 36865, 36872, 36873, 36928, 36929, 36936, 36937, 37376, 37377, 37384, 37385, 37440, 37441, 37448, 37449, 262144, 262145, 262152, 262153, 262208, 262209, 262216, 262217, 262656, 262657, 262664, 262665, 262720, 262721, 262728, 262729, 266240, 266241, 266248, 266249, 266304, 266305, 266312, 266313, 266752, 266753, 266760, 266761, 266816, 266817, 266824, 266825, 294912, 294913, 294920, 294921, 294976, 294977, 294984, 294985, 295424, 295425, 295432, 295433, 295488, 295489, 295496, 295497, 299008, 299009, 299016, 299017, 299072, 299073, 299080, 299081, 299520, 299521, 299528, 299529, 299584, 299585, 299592, 299593 };
        INTERLEAVE4_TABLE = new int[] { 0, 1, 16, 17, 256, 257, 272, 273, 4096, 4097, 4112, 4113, 4352, 4353, 4368, 4369, 65536, 65537, 65552, 65553, 65792, 65793, 65808, 65809, 69632, 69633, 69648, 69649, 69888, 69889, 69904, 69905, 1048576, 1048577, 1048592, 1048593, 1048832, 1048833, 1048848, 1048849, 1052672, 1052673, 1052688, 1052689, 1052928, 1052929, 1052944, 1052945, 1114112, 1114113, 1114128, 1114129, 1114368, 1114369, 1114384, 1114385, 1118208, 1118209, 1118224, 1118225, 1118464, 1118465, 1118480, 1118481, 16777216, 16777217, 16777232, 16777233, 16777472, 16777473, 16777488, 16777489, 16781312, 16781313, 16781328, 16781329, 16781568, 16781569, 16781584, 16781585, 16842752, 16842753, 16842768, 16842769, 16843008, 16843009, 16843024, 16843025, 16846848, 16846849, 16846864, 16846865, 16847104, 16847105, 16847120, 16847121, 17825792, 17825793, 17825808, 17825809, 17826048, 17826049, 17826064, 17826065, 17829888, 17829889, 17829904, 17829905, 17830144, 17830145, 17830160, 17830161, 17891328, 17891329, 17891344, 17891345, 17891584, 17891585, 17891600, 17891601, 17895424, 17895425, 17895440, 17895441, 17895680, 17895681, 17895696, 17895697, 268435456, 268435457, 268435472, 268435473, 268435712, 268435713, 268435728, 268435729, 268439552, 268439553, 268439568, 268439569, 268439808, 268439809, 268439824, 268439825, 268500992, 268500993, 268501008, 268501009, 268501248, 268501249, 268501264, 268501265, 268505088, 268505089, 268505104, 268505105, 268505344, 268505345, 268505360, 268505361, 269484032, 269484033, 269484048, 269484049, 269484288, 269484289, 269484304, 269484305, 269488128, 269488129, 269488144, 269488145, 269488384, 269488385, 269488400, 269488401, 269549568, 269549569, 269549584, 269549585, 269549824, 269549825, 269549840, 269549841, 269553664, 269553665, 269553680, 269553681, 269553920, 269553921, 269553936, 269553937, 285212672, 285212673, 285212688, 285212689, 285212928, 285212929, 285212944, 285212945, 285216768, 285216769, 285216784, 285216785, 285217024, 285217025, 285217040, 285217041, 285278208, 285278209, 285278224, 285278225, 285278464, 285278465, 285278480, 285278481, 285282304, 285282305, 285282320, 285282321, 285282560, 285282561, 285282576, 285282577, 286261248, 286261249, 286261264, 286261265, 286261504, 286261505, 286261520, 286261521, 286265344, 286265345, 286265360, 286265361, 286265600, 286265601, 286265616, 286265617, 286326784, 286326785, 286326800, 286326801, 286327040, 286327041, 286327056, 286327057, 286330880, 286330881, 286330896, 286330897, 286331136, 286331137, 286331152, 286331153 };
        INTERLEAVE5_TABLE = new int[] { 0, 1, 32, 33, 1024, 1025, 1056, 1057, 32768, 32769, 32800, 32801, 33792, 33793, 33824, 33825, 1048576, 1048577, 1048608, 1048609, 1049600, 1049601, 1049632, 1049633, 1081344, 1081345, 1081376, 1081377, 1082368, 1082369, 1082400, 1082401, 33554432, 33554433, 33554464, 33554465, 33555456, 33555457, 33555488, 33555489, 33587200, 33587201, 33587232, 33587233, 33588224, 33588225, 33588256, 33588257, 34603008, 34603009, 34603040, 34603041, 34604032, 34604033, 34604064, 34604065, 34635776, 34635777, 34635808, 34635809, 34636800, 34636801, 34636832, 34636833, 1073741824, 1073741825, 1073741856, 1073741857, 1073742848, 1073742849, 1073742880, 1073742881, 1073774592, 1073774593, 1073774624, 1073774625, 1073775616, 1073775617, 1073775648, 1073775649, 1074790400, 1074790401, 1074790432, 1074790433, 1074791424, 1074791425, 1074791456, 1074791457, 1074823168, 1074823169, 1074823200, 1074823201, 1074824192, 1074824193, 1074824224, 1074824225, 1107296256, 1107296257, 1107296288, 1107296289, 1107297280, 1107297281, 1107297312, 1107297313, 1107329024, 1107329025, 1107329056, 1107329057, 1107330048, 1107330049, 1107330080, 1107330081, 1108344832, 1108344833, 1108344864, 1108344865, 1108345856, 1108345857, 1108345888, 1108345889, 1108377600, 1108377601, 1108377632, 1108377633, 1108378624, 1108378625, 1108378656, 1108378657 };
        INTERLEAVE7_TABLE = new long[] { 0L, 1L, 128L, 129L, 16384L, 16385L, 16512L, 16513L, 2097152L, 2097153L, 2097280L, 2097281L, 2113536L, 2113537L, 2113664L, 2113665L, 268435456L, 268435457L, 268435584L, 268435585L, 268451840L, 268451841L, 268451968L, 268451969L, 270532608L, 270532609L, 270532736L, 270532737L, 270548992L, 270548993L, 270549120L, 270549121L, 34359738368L, 34359738369L, 34359738496L, 34359738497L, 34359754752L, 34359754753L, 34359754880L, 34359754881L, 34361835520L, 34361835521L, 34361835648L, 34361835649L, 34361851904L, 34361851905L, 34361852032L, 34361852033L, 34628173824L, 34628173825L, 34628173952L, 34628173953L, 34628190208L, 34628190209L, 34628190336L, 34628190337L, 34630270976L, 34630270977L, 34630271104L, 34630271105L, 34630287360L, 34630287361L, 34630287488L, 34630287489L, 4398046511104L, 4398046511105L, 4398046511232L, 4398046511233L, 4398046527488L, 4398046527489L, 4398046527616L, 4398046527617L, 4398048608256L, 4398048608257L, 4398048608384L, 4398048608385L, 4398048624640L, 4398048624641L, 4398048624768L, 4398048624769L, 4398314946560L, 4398314946561L, 4398314946688L, 4398314946689L, 4398314962944L, 4398314962945L, 4398314963072L, 4398314963073L, 4398317043712L, 4398317043713L, 4398317043840L, 4398317043841L, 4398317060096L, 4398317060097L, 4398317060224L, 4398317060225L, 4432406249472L, 4432406249473L, 4432406249600L, 4432406249601L, 4432406265856L, 4432406265857L, 4432406265984L, 4432406265985L, 4432408346624L, 4432408346625L, 4432408346752L, 4432408346753L, 4432408363008L, 4432408363009L, 4432408363136L, 4432408363137L, 4432674684928L, 4432674684929L, 4432674685056L, 4432674685057L, 4432674701312L, 4432674701313L, 4432674701440L, 4432674701441L, 4432676782080L, 4432676782081L, 4432676782208L, 4432676782209L, 4432676798464L, 4432676798465L, 4432676798592L, 4432676798593L, 562949953421312L, 562949953421313L, 562949953421440L, 562949953421441L, 562949953437696L, 562949953437697L, 562949953437824L, 562949953437825L, 562949955518464L, 562949955518465L, 562949955518592L, 562949955518593L, 562949955534848L, 562949955534849L, 562949955534976L, 562949955534977L, 562950221856768L, 562950221856769L, 562950221856896L, 562950221856897L, 562950221873152L, 562950221873153L, 562950221873280L, 562950221873281L, 562950223953920L, 562950223953921L, 562950223954048L, 562950223954049L, 562950223970304L, 562950223970305L, 562950223970432L, 562950223970433L, 562984313159680L, 562984313159681L, 562984313159808L, 562984313159809L, 562984313176064L, 562984313176065L, 562984313176192L, 562984313176193L, 562984315256832L, 562984315256833L, 562984315256960L, 562984315256961L, 562984315273216L, 562984315273217L, 562984315273344L, 562984315273345L, 562984581595136L, 562984581595137L, 562984581595264L, 562984581595265L, 562984581611520L, 562984581611521L, 562984581611648L, 562984581611649L, 562984583692288L, 562984583692289L, 562984583692416L, 562984583692417L, 562984583708672L, 562984583708673L, 562984583708800L, 562984583708801L, 567347999932416L, 567347999932417L, 567347999932544L, 567347999932545L, 567347999948800L, 567347999948801L, 567347999948928L, 567347999948929L, 567348002029568L, 567348002029569L, 567348002029696L, 567348002029697L, 567348002045952L, 567348002045953L, 567348002046080L, 567348002046081L, 567348268367872L, 567348268367873L, 567348268368000L, 567348268368001L, 567348268384256L, 567348268384257L, 567348268384384L, 567348268384385L, 567348270465024L, 567348270465025L, 567348270465152L, 567348270465153L, 567348270481408L, 567348270481409L, 567348270481536L, 567348270481537L, 567382359670784L, 567382359670785L, 567382359670912L, 567382359670913L, 567382359687168L, 567382359687169L, 567382359687296L, 567382359687297L, 567382361767936L, 567382361767937L, 567382361768064L, 567382361768065L, 567382361784320L, 567382361784321L, 567382361784448L, 567382361784449L, 567382628106240L, 567382628106241L, 567382628106368L, 567382628106369L, 567382628122624L, 567382628122625L, 567382628122752L, 567382628122753L, 567382630203392L, 567382630203393L, 567382630203520L, 567382630203521L, 567382630219776L, 567382630219777L, 567382630219904L, 567382630219905L, 72057594037927936L, 72057594037927937L, 72057594037928064L, 72057594037928065L, 72057594037944320L, 72057594037944321L, 72057594037944448L, 72057594037944449L, 72057594040025088L, 72057594040025089L, 72057594040025216L, 72057594040025217L, 72057594040041472L, 72057594040041473L, 72057594040041600L, 72057594040041601L, 72057594306363392L, 72057594306363393L, 72057594306363520L, 72057594306363521L, 72057594306379776L, 72057594306379777L, 72057594306379904L, 72057594306379905L, 72057594308460544L, 72057594308460545L, 72057594308460672L, 72057594308460673L, 72057594308476928L, 72057594308476929L, 72057594308477056L, 72057594308477057L, 72057628397666304L, 72057628397666305L, 72057628397666432L, 72057628397666433L, 72057628397682688L, 72057628397682689L, 72057628397682816L, 72057628397682817L, 72057628399763456L, 72057628399763457L, 72057628399763584L, 72057628399763585L, 72057628399779840L, 72057628399779841L, 72057628399779968L, 72057628399779969L, 72057628666101760L, 72057628666101761L, 72057628666101888L, 72057628666101889L, 72057628666118144L, 72057628666118145L, 72057628666118272L, 72057628666118273L, 72057628668198912L, 72057628668198913L, 72057628668199040L, 72057628668199041L, 72057628668215296L, 72057628668215297L, 72057628668215424L, 72057628668215425L, 72061992084439040L, 72061992084439041L, 72061992084439168L, 72061992084439169L, 72061992084455424L, 72061992084455425L, 72061992084455552L, 72061992084455553L, 72061992086536192L, 72061992086536193L, 72061992086536320L, 72061992086536321L, 72061992086552576L, 72061992086552577L, 72061992086552704L, 72061992086552705L, 72061992352874496L, 72061992352874497L, 72061992352874624L, 72061992352874625L, 72061992352890880L, 72061992352890881L, 72061992352891008L, 72061992352891009L, 72061992354971648L, 72061992354971649L, 72061992354971776L, 72061992354971777L, 72061992354988032L, 72061992354988033L, 72061992354988160L, 72061992354988161L, 72062026444177408L, 72062026444177409L, 72062026444177536L, 72062026444177537L, 72062026444193792L, 72062026444193793L, 72062026444193920L, 72062026444193921L, 72062026446274560L, 72062026446274561L, 72062026446274688L, 72062026446274689L, 72062026446290944L, 72062026446290945L, 72062026446291072L, 72062026446291073L, 72062026712612864L, 72062026712612865L, 72062026712612992L, 72062026712612993L, 72062026712629248L, 72062026712629249L, 72062026712629376L, 72062026712629377L, 72062026714710016L, 72062026714710017L, 72062026714710144L, 72062026714710145L, 72062026714726400L, 72062026714726401L, 72062026714726528L, 72062026714726529L, 72620543991349248L, 72620543991349249L, 72620543991349376L, 72620543991349377L, 72620543991365632L, 72620543991365633L, 72620543991365760L, 72620543991365761L, 72620543993446400L, 72620543993446401L, 72620543993446528L, 72620543993446529L, 72620543993462784L, 72620543993462785L, 72620543993462912L, 72620543993462913L, 72620544259784704L, 72620544259784705L, 72620544259784832L, 72620544259784833L, 72620544259801088L, 72620544259801089L, 72620544259801216L, 72620544259801217L, 72620544261881856L, 72620544261881857L, 72620544261881984L, 72620544261881985L, 72620544261898240L, 72620544261898241L, 72620544261898368L, 72620544261898369L, 72620578351087616L, 72620578351087617L, 72620578351087744L, 72620578351087745L, 72620578351104000L, 72620578351104001L, 72620578351104128L, 72620578351104129L, 72620578353184768L, 72620578353184769L, 72620578353184896L, 72620578353184897L, 72620578353201152L, 72620578353201153L, 72620578353201280L, 72620578353201281L, 72620578619523072L, 72620578619523073L, 72620578619523200L, 72620578619523201L, 72620578619539456L, 72620578619539457L, 72620578619539584L, 72620578619539585L, 72620578621620224L, 72620578621620225L, 72620578621620352L, 72620578621620353L, 72620578621636608L, 72620578621636609L, 72620578621636736L, 72620578621636737L, 72624942037860352L, 72624942037860353L, 72624942037860480L, 72624942037860481L, 72624942037876736L, 72624942037876737L, 72624942037876864L, 72624942037876865L, 72624942039957504L, 72624942039957505L, 72624942039957632L, 72624942039957633L, 72624942039973888L, 72624942039973889L, 72624942039974016L, 72624942039974017L, 72624942306295808L, 72624942306295809L, 72624942306295936L, 72624942306295937L, 72624942306312192L, 72624942306312193L, 72624942306312320L, 72624942306312321L, 72624942308392960L, 72624942308392961L, 72624942308393088L, 72624942308393089L, 72624942308409344L, 72624942308409345L, 72624942308409472L, 72624942308409473L, 72624976397598720L, 72624976397598721L, 72624976397598848L, 72624976397598849L, 72624976397615104L, 72624976397615105L, 72624976397615232L, 72624976397615233L, 72624976399695872L, 72624976399695873L, 72624976399696000L, 72624976399696001L, 72624976399712256L, 72624976399712257L, 72624976399712384L, 72624976399712385L, 72624976666034176L, 72624976666034177L, 72624976666034304L, 72624976666034305L, 72624976666050560L, 72624976666050561L, 72624976666050688L, 72624976666050689L, 72624976668131328L, 72624976668131329L, 72624976668131456L, 72624976668131457L, 72624976668147712L, 72624976668147713L, 72624976668147840L, 72624976668147841L };
        bitLengths = new byte[] { 0, 1, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
    }
    
    public LongArray(final int n) {
        this.m_ints = new long[n];
    }
    
    public LongArray(final BigInteger bigInteger) {
        if (bigInteger == null || bigInteger.signum() < 0) {
            throw new IllegalArgumentException("invalid F2m field value");
        }
        if (bigInteger.signum() == 0) {
            this.m_ints = new long[] { 0L };
            return;
        }
        final byte[] byteArray = bigInteger.toByteArray();
        int length = byteArray.length;
        int i;
        if (byteArray[0] == 0) {
            --length;
            i = 1;
        }
        else {
            i = 0;
        }
        final int n = (length + 7) / 8;
        this.m_ints = new long[n];
        final int n2 = n - 1;
        final int n3 = length % 8 + i;
        int n4 = i;
        int j = n2;
        if (i < n3) {
            long n5;
            long n6;
            for (n5 = 0L; i < n3; ++i, n5 = (n5 << 8 | n6)) {
                n6 = (byteArray[i] & 0xFF);
            }
            this.m_ints[n2] = n5;
            j = n2 - 1;
            n4 = i;
        }
        while (j >= 0) {
            int n7;
            int k;
            long n8;
            long n9;
            for (n7 = n4, k = 0, n8 = 0L; k < 8; ++k, ++n7, n8 = (n8 << 8 | n9)) {
                n9 = (byteArray[n7] & 0xFF);
            }
            this.m_ints[j] = n8;
            --j;
            n4 = n7;
        }
    }
    
    public LongArray(final long[] ints) {
        this.m_ints = ints;
    }
    
    public LongArray(final long[] ints, final int n, final int n2) {
        if (n == 0 && n2 == ints.length) {
            this.m_ints = ints;
            return;
        }
        System.arraycopy(ints, n, this.m_ints = new long[n2], 0, n2);
    }
    
    private static void add(final long[] array, final int n, final long[] array2, final int n2, final int n3) {
        for (int i = 0; i < n3; ++i) {
            final int n4 = n + i;
            array[n4] ^= array2[n2 + i];
        }
    }
    
    private static void add(final long[] array, final int n, final long[] array2, final int n2, final long[] array3, final int n3, final int n4) {
        for (int i = 0; i < n4; ++i) {
            array3[n3 + i] = (array[n + i] ^ array2[n2 + i]);
        }
    }
    
    private static void addBoth(final long[] array, final int n, final long[] array2, final int n2, final long[] array3, final int n3, final int n4) {
        for (int i = 0; i < n4; ++i) {
            final int n5 = n + i;
            array[n5] ^= (array2[n2 + i] ^ array3[n3 + i]);
        }
    }
    
    private void addShiftedByBitsSafe(final LongArray longArray, int n, int n2) {
        n = n + 63 >>> 6;
        final int n3 = n2 >>> 6;
        n2 &= 0x3F;
        if (n2 == 0) {
            add(this.m_ints, n3, longArray.m_ints, 0, n);
            return;
        }
        final long addShiftedUp = addShiftedUp(this.m_ints, n3, longArray.m_ints, 0, n, n2);
        if (addShiftedUp != 0L) {
            final long[] ints = this.m_ints;
            n += n3;
            ints[n] ^= addShiftedUp;
        }
    }
    
    private static long addShiftedDown(final long[] array, final int n, final long[] array2, final int n2, int n3, final int n4) {
        long n5 = 0L;
        while (true) {
            --n3;
            if (n3 < 0) {
                break;
            }
            final long n6 = array2[n2 + n3];
            final int n7 = n + n3;
            array[n7] ^= (n5 | n6 >>> n4);
            n5 = n6 << 64 - n4;
        }
        return n5;
    }
    
    private static long addShiftedUp(final long[] array, final int n, final long[] array2, final int n2, final int n3, final int n4) {
        long n5 = 0L;
        for (int i = 0; i < n3; ++i) {
            final long n6 = array2[n2 + i];
            final int n7 = n + i;
            array[n7] ^= (n5 | n6 << n4);
            n5 = n6 >>> 64 - n4;
        }
        return n5;
    }
    
    private static int bitLength(final long n) {
        int n2 = 32;
        int n3;
        if ((n3 = (int)(n >>> 32)) == 0) {
            n3 = (int)n;
            n2 = 0;
        }
        final int n4 = n3 >>> 16;
        int n6;
        if (n4 == 0) {
            final int n5 = n3 >>> 8;
            if (n5 == 0) {
                n6 = LongArray.bitLengths[n3];
            }
            else {
                n6 = LongArray.bitLengths[n5] + 8;
            }
        }
        else {
            final int n7 = n4 >>> 8;
            if (n7 == 0) {
                n6 = LongArray.bitLengths[n4] + 16;
            }
            else {
                n6 = LongArray.bitLengths[n7] + 24;
            }
        }
        return n2 + n6;
    }
    
    private int degreeFrom(int i) {
        i = i + 62 >>> 6;
        while (i != 0) {
            final long[] ints = this.m_ints;
            final int n = i - 1;
            final long n2 = ints[n];
            i = n;
            if (n2 != 0L) {
                return (n << 6) + bitLength(n2);
            }
        }
        return 0;
    }
    
    private static void distribute(final long[] array, final int n, final int n2, final int n3, final int n4) {
        for (int i = 0; i < n4; ++i) {
            final long n5 = array[n + i];
            final int n6 = n2 + i;
            array[n6] ^= n5;
            final int n7 = n3 + i;
            array[n7] ^= n5;
        }
    }
    
    private static void flipBit(final long[] array, int n, final int n2) {
        n += n2 >>> 6;
        array[n] ^= 1L << (n2 & 0x3F);
    }
    
    private static void flipVector(final long[] array, int n, final long[] array2, final int n2, final int n3, int n4) {
        n += n4 >>> 6;
        n4 &= 0x3F;
        if (n4 == 0) {
            add(array, n, array2, n2, n3);
            return;
        }
        array[n] ^= addShiftedDown(array, n + 1, array2, n2, n3, 64 - n4);
    }
    
    private static void flipWord(final long[] array, int n, int n2, long n3) {
        n += n2 >>> 6;
        n2 &= 0x3F;
        if (n2 == 0) {
            array[n] ^= n3;
            return;
        }
        array[n] ^= n3 << n2;
        n3 >>>= 64 - n2;
        if (n3 != 0L) {
            ++n;
            array[n] ^= n3;
        }
    }
    
    private static void interleave(final long[] array, final int n, final long[] array2, final int n2, final int n3, final int n4) {
        if (n4 == 3) {
            interleave3(array, n, array2, n2, n3);
            return;
        }
        if (n4 == 5) {
            interleave5(array, n, array2, n2, n3);
            return;
        }
        if (n4 != 7) {
            interleave2_n(array, n, array2, n2, n3, LongArray.bitLengths[n4] - 1);
            return;
        }
        interleave7(array, n, array2, n2, n3);
    }
    
    private static long interleave2_32to64(final int n) {
        final short[] interleave2_TABLE = LongArray.INTERLEAVE2_TABLE;
        return ((long)(interleave2_TABLE[n >>> 8 & 0xFF] << 16 | interleave2_TABLE[n & 0xFF]) & 0xFFFFFFFFL) | ((long)(interleave2_TABLE[n >>> 24] << 16 | interleave2_TABLE[n >>> 16 & 0xFF]) & 0xFFFFFFFFL) << 32;
    }
    
    private static long interleave2_n(long n, int i) {
        while (i > 1) {
            i -= 2;
            n = (interleave4_16to64((int)(n >>> 48) & 0xFFFF) << 3 | (interleave4_16to64((int)(n >>> 16) & 0xFFFF) << 1 | interleave4_16to64((int)n & 0xFFFF) | interleave4_16to64((int)(n >>> 32) & 0xFFFF) << 2));
        }
        long n2 = n;
        if (i > 0) {
            n2 = (interleave2_32to64((int)(n >>> 32)) << 1 | interleave2_32to64((int)n));
        }
        return n2;
    }
    
    private static void interleave2_n(final long[] array, final int n, final long[] array2, final int n2, final int n3, final int n4) {
        for (int i = 0; i < n3; ++i) {
            array2[n2 + i] = interleave2_n(array[n + i], n4);
        }
    }
    
    private static long interleave3(final long n) {
        return interleave3_21to63((int)(n >>> 42) & 0x1FFFFF) << 2 | ((n & Long.MIN_VALUE) | interleave3_21to63((int)n & 0x1FFFFF) | interleave3_21to63((int)(n >>> 21) & 0x1FFFFF) << 1);
    }
    
    private static void interleave3(final long[] array, final int n, final long[] array2, final int n2, final int n3) {
        for (int i = 0; i < n3; ++i) {
            array2[n2 + i] = interleave3(array[n + i]);
        }
    }
    
    private static long interleave3_13to65(final int n) {
        final int[] interleave5_TABLE = LongArray.INTERLEAVE5_TABLE;
        return ((long)interleave5_TABLE[n & 0x7F] & 0xFFFFFFFFL) | ((long)interleave5_TABLE[n >>> 7] & 0xFFFFFFFFL) << 35;
    }
    
    private static long interleave3_21to63(final int n) {
        final int[] interleave3_TABLE = LongArray.INTERLEAVE3_TABLE;
        return ((long)interleave3_TABLE[n & 0x7F] & 0xFFFFFFFFL) | (((long)interleave3_TABLE[n >>> 14] & 0xFFFFFFFFL) << 42 | ((long)interleave3_TABLE[n >>> 7 & 0x7F] & 0xFFFFFFFFL) << 21);
    }
    
    private static long interleave4_16to64(final int n) {
        final int[] interleave4_TABLE = LongArray.INTERLEAVE4_TABLE;
        return ((long)interleave4_TABLE[n & 0xFF] & 0xFFFFFFFFL) | ((long)interleave4_TABLE[n >>> 8] & 0xFFFFFFFFL) << 32;
    }
    
    private static long interleave5(final long n) {
        return interleave3_13to65((int)(n >>> 52) & 0x1FFF) << 4 | (interleave3_13to65((int)n & 0x1FFF) | interleave3_13to65((int)(n >>> 13) & 0x1FFF) << 1 | interleave3_13to65((int)(n >>> 26) & 0x1FFF) << 2 | interleave3_13to65((int)(n >>> 39) & 0x1FFF) << 3);
    }
    
    private static void interleave5(final long[] array, final int n, final long[] array2, final int n2, final int n3) {
        for (int i = 0; i < n3; ++i) {
            array2[n2 + i] = interleave5(array[n + i]);
        }
    }
    
    private static long interleave7(final long n) {
        final long[] interleave7_TABLE = LongArray.INTERLEAVE7_TABLE;
        return interleave7_TABLE[(int)(n >>> 54) & 0x1FF] << 6 | ((n & Long.MIN_VALUE) | interleave7_TABLE[(int)n & 0x1FF] | interleave7_TABLE[(int)(n >>> 9) & 0x1FF] << 1 | interleave7_TABLE[(int)(n >>> 18) & 0x1FF] << 2 | interleave7_TABLE[(int)(n >>> 27) & 0x1FF] << 3 | interleave7_TABLE[(int)(n >>> 36) & 0x1FF] << 4 | interleave7_TABLE[(int)(n >>> 45) & 0x1FF] << 5);
    }
    
    private static void interleave7(final long[] array, final int n, final long[] array2, final int n2, final int n3) {
        for (int i = 0; i < n3; ++i) {
            array2[n2 + i] = interleave7(array[n + i]);
        }
    }
    
    private static void multiplyWord(long n, final long[] array, final int n2, final long[] array2, final int n3) {
        if ((n & 0x1L) != 0x0L) {
            add(array2, n3, array, 0, n2);
        }
        int n4 = 1;
        while (true) {
            n >>>= 1;
            if (n == 0L) {
                break;
            }
            if ((n & 0x1L) != 0x0L) {
                final long addShiftedUp = addShiftedUp(array2, n3, array, 0, n2, n4);
                if (addShiftedUp != 0L) {
                    final int n5 = n3 + n2;
                    array2[n5] ^= addShiftedUp;
                }
            }
            ++n4;
        }
    }
    
    private static void reduceBit(final long[] array, final int n, int length, int n2, final int[] array2) {
        flipBit(array, n, length);
        n2 = length - n2;
        length = array2.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            flipBit(array, n, array2[length] + n2);
        }
        flipBit(array, n, n2);
    }
    
    private static void reduceBitWise(final long[] array, final int n, int n2, final int n3, final int[] array2) {
        while (true) {
            final int n4 = n2 - 1;
            if (n4 < n3) {
                break;
            }
            n2 = n4;
            if (!testBit(array, n, n4)) {
                continue;
            }
            reduceBit(array, n, n4, n3, array2);
            n2 = n4;
        }
    }
    
    private static int reduceInPlace(final long[] array, final int n, int i, final int n2, final int[] array2) {
        final int n3 = n2 + 63 >>> 6;
        if (i < n3) {
            return i;
        }
        final int n4 = i << 6;
        final int min = Math.min(n4, (n2 << 1) - 1);
        int j;
        for (j = n4 - min; j >= 64; j -= 64) {
            --i;
        }
        final int length = array2.length;
        final int n5 = array2[length - 1];
        int n6;
        if (length > 1) {
            n6 = array2[length - 2];
        }
        else {
            n6 = 0;
        }
        final int max = Math.max(n2, n5 + 64);
        final int n7 = j + Math.min(min - max, n2 - n6) >> 6;
        int n8 = min;
        int n9 = i;
        if (n7 > 1) {
            final int n10 = i - n7;
            reduceVectorWise(array, n, i, n10, n2, array2);
            while (i > n10) {
                --i;
                array[n + i] = 0L;
            }
            n8 = n10 << 6;
            n9 = i;
        }
        if (n8 > max) {
            reduceWordWise(array, n, n9, max, n2, array2);
            n8 = max;
        }
        if (n8 > n2) {
            reduceBitWise(array, n, n8, n2, array2);
        }
        return n3;
    }
    
    private static LongArray reduceResult(final long[] array, final int n, final int n2, final int n3, final int[] array2) {
        return new LongArray(array, n, reduceInPlace(array, n, n2, n3, array2));
    }
    
    private static void reduceVectorWise(final long[] array, final int n, final int n2, final int n3, int length, final int[] array2) {
        final int n4 = (n3 << 6) - length;
        length = array2.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            flipVector(array, n, array, n + n3, n2 - n3, n4 + array2[length]);
        }
        flipVector(array, n, array, n + n3, n2 - n3, n4);
    }
    
    private static void reduceWord(final long[] array, final int n, int length, final long n2, int n3, final int[] array2) {
        n3 = length - n3;
        length = array2.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            flipWord(array, n, array2[length] + n3, n2);
        }
        flipWord(array, n, n3, n2);
    }
    
    private static void reduceWordWise(final long[] array, final int n, int n2, final int n3, final int n4, final int[] array2) {
        final int n5 = n3 >>> 6;
        while (true) {
            --n2;
            if (n2 <= n5) {
                break;
            }
            final int n6 = n + n2;
            final long n7 = array[n6];
            if (n7 == 0L) {
                continue;
            }
            array[n6] = 0L;
            reduceWord(array, n, n2 << 6, n7, n4, array2);
        }
        n2 = (n3 & 0x3F);
        final int n8 = n + n5;
        final long n9 = array[n8] >>> n2;
        if (n9 != 0L) {
            array[n8] ^= n9 << n2;
            reduceWord(array, n, n3, n9, n4, array2);
        }
    }
    
    private long[] resizedInts(final int n) {
        final long[] array = new long[n];
        final long[] ints = this.m_ints;
        System.arraycopy(ints, 0, array, 0, Math.min(ints.length, n));
        return array;
    }
    
    private static long shiftUp(final long[] array, final int n, final int n2, final int n3) {
        long n4 = 0L;
        for (int i = 0; i < n2; ++i) {
            final int n5 = n + i;
            final long n6 = array[n5];
            array[n5] = (n4 | n6 << n3);
            n4 = n6 >>> 64 - n3;
        }
        return n4;
    }
    
    private static long shiftUp(final long[] array, final int n, final long[] array2, final int n2, final int n3, final int n4) {
        long n5 = 0L;
        for (int i = 0; i < n3; ++i) {
            final long n6 = array[n + i];
            array2[n2 + i] = (n5 | n6 << n4);
            n5 = n6 >>> 64 - n4;
        }
        return n5;
    }
    
    private static void squareInPlace(final long[] array, int n, int n2, final int[] array2) {
        n2 = n << 1;
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final long n3 = array[n];
            --n2;
            array[n2] = interleave2_32to64((int)(n3 >>> 32));
            --n2;
            array[n2] = interleave2_32to64((int)n3);
        }
    }
    
    private static boolean testBit(final long[] array, final int n, final int n2) {
        return (array[n + (n2 >>> 6)] & 1L << (n2 & 0x3F)) != 0x0L;
    }
    
    public LongArray addOne() {
        if (this.m_ints.length == 0) {
            return new LongArray(new long[] { 1L });
        }
        final long[] resizedInts = this.resizedInts(Math.max(1, this.getUsedLength()));
        resizedInts[0] ^= 0x1L;
        return new LongArray(resizedInts);
    }
    
    public void addShiftedByWords(final LongArray longArray, final int n) {
        final int usedLength = longArray.getUsedLength();
        if (usedLength == 0) {
            return;
        }
        final int n2 = usedLength + n;
        if (n2 > this.m_ints.length) {
            this.m_ints = this.resizedInts(n2);
        }
        add(this.m_ints, n, longArray.m_ints, 0, usedLength);
    }
    
    public Object clone() {
        return new LongArray(Arrays.clone(this.m_ints));
    }
    
    public int degree() {
        int i = this.m_ints.length;
        while (i != 0) {
            final long[] ints = this.m_ints;
            final int n = i - 1;
            final long n2 = ints[n];
            i = n;
            if (n2 != 0L) {
                return (n << 6) + bitLength(n2);
            }
        }
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof LongArray)) {
            return false;
        }
        final LongArray longArray = (LongArray)o;
        final int usedLength = this.getUsedLength();
        if (longArray.getUsedLength() != usedLength) {
            return false;
        }
        for (int i = 0; i < usedLength; ++i) {
            if (this.m_ints[i] != longArray.m_ints[i]) {
                return false;
            }
        }
        return true;
    }
    
    public int getLength() {
        return this.m_ints.length;
    }
    
    public int getUsedLength() {
        return this.getUsedLengthFrom(this.m_ints.length);
    }
    
    public int getUsedLengthFrom(int n) {
        final long[] ints = this.m_ints;
        int min = Math.min(n, ints.length);
        if (min < 1) {
            return 0;
        }
        n = min;
        if (ints[0] != 0L) {
            do {
                --min;
            } while (ints[min] == 0L);
            return min + 1;
        }
        int n2;
        do {
            n2 = n - 1;
            if (ints[n2] != 0L) {
                return n2 + 1;
            }
        } while ((n = n2) > 0);
        return 0;
    }
    
    @Override
    public int hashCode() {
        final int usedLength = this.getUsedLength();
        int n = 1;
        for (int i = 0; i < usedLength; ++i) {
            final long n2 = this.m_ints[i];
            n = ((n * 31 ^ (int)n2) * 31 ^ (int)(n2 >>> 32));
        }
        return n;
    }
    
    public boolean isOne() {
        final long[] ints = this.m_ints;
        if (ints[0] != 1L) {
            return false;
        }
        for (int i = 1; i < ints.length; ++i) {
            if (ints[i] != 0L) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isZero() {
        final long[] ints = this.m_ints;
        for (int i = 0; i < ints.length; ++i) {
            if (ints[i] != 0L) {
                return false;
            }
        }
        return true;
    }
    
    public LongArray modInverse(int degree, int[] array) {
        final int degree2 = this.degree();
        if (degree2 == 0) {
            throw new IllegalStateException();
        }
        if (degree2 == 1) {
            return this;
        }
        final LongArray longArray = (LongArray)this.clone();
        final int n = degree + 63 >>> 6;
        final LongArray longArray2 = new LongArray(n);
        reduceBit(longArray2.m_ints, 0, degree, degree, array);
        final LongArray longArray3 = new LongArray(n);
        longArray3.m_ints[0] = 1L;
        final LongArray longArray4 = new LongArray(n);
        array = new int[] { degree2, degree + 1 };
        final LongArray[] array2 = { longArray, longArray2 };
        final int[] array4;
        final int[] array3 = array4 = new int[2];
        array4[array4[0] = 1] = 0;
        final LongArray[] array5 = { longArray3, longArray4 };
        int degree3 = array[1];
        degree = array3[1];
        int n2 = degree3 - array[0];
        int n3 = 1;
        while (true) {
            int n4 = degree;
            int n5 = n2;
            int n6 = degree3;
            int n7 = n3;
            if (n2 < 0) {
                n5 = -n2;
                array[n3] = degree3;
                array3[n3] = degree;
                n7 = 1 - n3;
                n6 = array[n7];
                n4 = array3[n7];
            }
            final LongArray longArray5 = array2[n7];
            degree = 1 - n7;
            longArray5.addShiftedByBitsSafe(array2[degree], array[degree], n5);
            degree3 = array2[n7].degreeFrom(n6);
            if (degree3 == 0) {
                break;
            }
            final int n8 = array3[degree];
            array5[n7].addShiftedByBitsSafe(array5[degree], n8, n5);
            final int n9 = n8 + n5;
            if (n9 > n4) {
                degree = n9;
            }
            else if (n9 == (degree = n4)) {
                degree = array5[n7].degreeFrom(n4);
            }
            n2 = n5 + (degree3 - n6);
            n3 = n7;
        }
        return array5[degree];
    }
    
    public LongArray modMultiply(LongArray longArray, final int n, final int[] array) {
        int degree = this.degree();
        if (degree == 0) {
            return this;
        }
        final int degree2 = longArray.degree();
        if (degree2 == 0) {
            return longArray;
        }
        int n2;
        LongArray longArray2;
        if (degree > degree2) {
            n2 = degree;
            degree = degree2;
            longArray2 = longArray;
            longArray = this;
        }
        else {
            longArray2 = this;
            n2 = degree2;
        }
        final int n3 = degree + 63 >>> 6;
        final int n4 = n2 + 63 >>> 6;
        final int n5 = degree + n2 + 62 >>> 6;
        if (n3 != 1) {
            final int n6 = n2 + 7 + 63 >>> 6;
            final int[] array2 = new int[16];
            final int n7 = n6 << 4;
            final long[] array3 = new long[n7];
            array2[1] = n6;
            System.arraycopy(longArray.m_ints, 0, array3, n6, n4);
            int i = 2;
            int n8 = n6;
            final long[] array4 = array3;
            while (i < 16) {
                n8 += n6;
                array2[i] = n8;
                if ((i & 0x1) == 0x0) {
                    shiftUp(array4, n8 >>> 1, array4, n8, n6, 1);
                }
                else {
                    final long[] array5 = array4;
                    add(array5, n6, array5, n8 - n6, array5, n8, n6);
                }
                ++i;
            }
            final long[] array6 = new long[n7];
            shiftUp(array4, 0, array6, 0, n7, 4);
            final long[] ints = longArray2.m_ints;
            final int n9 = n5 << 3;
            final long[] array7 = new long[n9];
            int n10 = 0;
            int n11;
            while (true) {
                n11 = n9;
                if (n10 >= n3) {
                    break;
                }
                long n12 = ints[n10];
                int n13 = n10;
                while (true) {
                    final int n14 = (int)n12;
                    final long n15 = n12 >>> 4;
                    addBoth(array7, n13, array4, array2[n14 & 0xF], array6, array2[(int)n15 & 0xF], n6);
                    n12 = n15 >>> 4;
                    if (n12 == 0L) {
                        break;
                    }
                    n13 += n5;
                }
                ++n10;
            }
            while (true) {
                n11 -= n5;
                if (n11 == 0) {
                    break;
                }
                addShiftedUp(array7, n11 - n5, array7, n11, n5, 8);
            }
            return reduceResult(array7, 0, n5, n, array);
        }
        final long n16 = longArray2.m_ints[0];
        if (n16 == 1L) {
            return longArray;
        }
        final long[] array8 = new long[n5];
        multiplyWord(n16, longArray.m_ints, n4, array8, 0);
        return reduceResult(array8, 0, n5, n, array);
    }
    
    public LongArray modMultiplyAlt(LongArray longArray, final int n, final int[] array) {
        int degree = this.degree();
        if (degree == 0) {
            return this;
        }
        final int degree2 = longArray.degree();
        if (degree2 == 0) {
            return longArray;
        }
        int n2;
        LongArray longArray2;
        if (degree > degree2) {
            n2 = degree;
            degree = degree2;
            longArray2 = longArray;
            longArray = this;
        }
        else {
            longArray2 = this;
            n2 = degree2;
        }
        final int n3 = degree + 63 >>> 6;
        final int n4 = n2 + 63 >>> 6;
        final int n5 = degree + n2 + 62 >>> 6;
        if (n3 != 1) {
            final int n6 = 15;
            final int n7 = n2 + 15 + 63 >>> 6;
            final int n8 = n7 * 8;
            final int[] array2 = new int[16];
            array2[0] = n3;
            int n9 = n3 + n8;
            array2[1] = n9;
            for (int i = 2; i < 16; ++i) {
                n9 += n5;
                array2[i] = n9;
            }
            final long[] array3 = new long[n9 + n5 + 1];
            final long[] ints = longArray2.m_ints;
            final int n10 = 16;
            final int n11 = n8;
            interleave(ints, 0, array3, 0, n3, 4);
            System.arraycopy(longArray.m_ints, 0, array3, n3, n4);
            int n12 = n3;
            for (int j = 1; j < 8; ++j) {
                n12 += n7;
                shiftUp(array3, n3, array3, n12, n7, j);
            }
            int n13 = 0;
            int n14 = n6;
            while (true) {
                int n15 = 0;
                do {
                    long n16 = array3[n15] >>> n13;
                    int n17 = n3;
                    int n18 = 0;
                    while (true) {
                        final int n19 = (int)n16 & n14;
                        if (n19 != 0) {
                            add(array3, array2[n19] + n15, array3, n17, n7);
                        }
                        ++n18;
                        if (n18 == 8) {
                            break;
                        }
                        n17 += n7;
                        n16 >>>= 4;
                    }
                } while (++n15 < n3);
                final int n20 = n13 + 32;
                int n21;
                int n22;
                if (n20 >= 64) {
                    if (n20 >= 64) {
                        break;
                    }
                    n21 = (n14 & n14 << 4);
                    n22 = 60;
                }
                else {
                    n21 = n14;
                    n22 = n20;
                }
                shiftUp(array3, n3, n11, 8);
                n13 = n22;
                n14 = n21;
            }
            int n23 = n10;
            while (true) {
                --n23;
                if (n23 <= 1) {
                    break;
                }
                if (((long)n23 & 0x1L) == 0x0L) {
                    addShiftedUp(array3, array2[n23 >>> 1], array3, array2[n23], n5, 16);
                }
                else {
                    distribute(array3, array2[n23], array2[n23 - 1], array2[1], n5);
                }
            }
            return reduceResult(array3, array2[1], n5, n, array);
        }
        final long n24 = longArray2.m_ints[0];
        if (n24 == 1L) {
            return longArray;
        }
        final long[] array4 = new long[n5];
        multiplyWord(n24, longArray.m_ints, n4, array4, 0);
        return reduceResult(array4, 0, n5, n, array);
    }
    
    public LongArray modMultiplyLD(LongArray longArray, final int n, final int[] array) {
        int degree = this.degree();
        if (degree == 0) {
            return this;
        }
        final int degree2 = longArray.degree();
        if (degree2 == 0) {
            return longArray;
        }
        int n2;
        LongArray longArray2;
        if (degree > degree2) {
            n2 = degree;
            degree = degree2;
            longArray2 = longArray;
            longArray = this;
        }
        else {
            longArray2 = this;
            n2 = degree2;
        }
        final int n3 = degree + 63 >>> 6;
        final int n4 = n2 + 63 >>> 6;
        final int n5 = degree + n2 + 62 >>> 6;
        if (n3 != 1) {
            final int n6 = n2 + 7 + 63 >>> 6;
            final int[] array2 = new int[16];
            final int n7 = n6 << 4;
            final long[] array3 = new long[n7];
            array2[1] = n6;
            System.arraycopy(longArray.m_ints, 0, array3, n6, n4);
            int i = 2;
            int n8 = n6;
            final long[] array4 = array3;
            while (i < 16) {
                n8 += n6;
                array2[i] = n8;
                if ((i & 0x1) == 0x0) {
                    shiftUp(array4, n8 >>> 1, array4, n8, n6, 1);
                }
                else {
                    final long[] array5 = array4;
                    add(array5, n6, array5, n8 - n6, array5, n8, n6);
                }
                ++i;
            }
            final long[] array6 = new long[n7];
            shiftUp(array4, 0, array6, 0, n7, 4);
            final long[] ints = longArray2.m_ints;
            final long[] array7 = new long[n5];
            final int n9 = 56;
            for (int j = 56; j >= 0; j -= 8) {
                for (int k = 1; k < n3; k += 2) {
                    final int n10 = (int)(ints[k] >>> j);
                    addBoth(array7, k - 1, array4, array2[n10 & 0xF], array6, array2[n10 >>> 4 & 0xF], n6);
                }
                shiftUp(array7, 0, n5, 8);
            }
            for (int l = n9; l >= 0; l -= 8) {
                for (int n11 = 0; n11 < n3; n11 += 2) {
                    final int n12 = (int)(ints[n11] >>> l);
                    addBoth(array7, n11, array4, array2[n12 & 0xF], array6, array2[n12 >>> 4 & 0xF], n6);
                }
                if (l > 0) {
                    shiftUp(array7, 0, n5, 8);
                }
            }
            return reduceResult(array7, 0, n5, n, array);
        }
        final long n13 = longArray2.m_ints[0];
        if (n13 == 1L) {
            return longArray;
        }
        final long[] array8 = new long[n5];
        multiplyWord(n13, longArray.m_ints, n4, array8, 0);
        return reduceResult(array8, 0, n5, n, array);
    }
    
    public LongArray modReduce(final int n, final int[] array) {
        final long[] clone = Arrays.clone(this.m_ints);
        return new LongArray(clone, 0, reduceInPlace(clone, 0, clone.length, n, array));
    }
    
    public LongArray modSquare(final int n, final int[] array) {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return this;
        }
        final int n2 = usedLength << 1;
        final long[] array2 = new long[n2];
        long n3;
        int n4;
        for (int i = 0; i < n2; i = n4 + 1, array2[n4] = interleave2_32to64((int)(n3 >>> 32))) {
            n3 = this.m_ints[i >>> 1];
            n4 = i + 1;
            array2[i] = interleave2_32to64((int)n3);
        }
        return new LongArray(array2, 0, reduceInPlace(array2, 0, n2, n, array));
    }
    
    public LongArray modSquareN(int reduceInPlace, final int n, final int[] array) {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return this;
        }
        final int n2 = n + 63 >>> 6 << 1;
        final long[] array2 = new long[n2];
        System.arraycopy(this.m_ints, 0, array2, 0, usedLength);
        int n3 = reduceInPlace;
        reduceInPlace = usedLength;
        while (true) {
            --n3;
            if (n3 < 0) {
                break;
            }
            squareInPlace(array2, reduceInPlace, n, array);
            reduceInPlace = reduceInPlace(array2, 0, n2, n, array);
        }
        return new LongArray(array2, 0, reduceInPlace);
    }
    
    public LongArray multiply(LongArray longArray, int n, final int[] array) {
        int degree = this.degree();
        if (degree == 0) {
            return this;
        }
        final int degree2 = longArray.degree();
        if (degree2 == 0) {
            return longArray;
        }
        LongArray longArray2;
        if (degree > degree2) {
            n = degree;
            degree = degree2;
            longArray2 = longArray;
            longArray = this;
        }
        else {
            longArray2 = this;
            n = degree2;
        }
        final int n2 = degree + 63 >>> 6;
        final int n3 = n + 63 >>> 6;
        final int n4 = degree + n + 62 >>> 6;
        if (n2 != 1) {
            final int n5 = n + 7 + 63 >>> 6;
            final int[] array2 = new int[16];
            n = n5 << 4;
            final long[] array3 = new long[n];
            array2[1] = n5;
            System.arraycopy(longArray.m_ints, 0, array3, n5, n3);
            int i = 2;
            int n6 = n5;
            final long[] array4 = array3;
            while (i < 16) {
                n6 += n5;
                array2[i] = n6;
                if ((i & 0x1) == 0x0) {
                    shiftUp(array4, n6 >>> 1, array4, n6, n5, 1);
                }
                else {
                    final long[] array5 = array4;
                    add(array5, n5, array5, n6 - n5, array5, n6, n5);
                }
                ++i;
            }
            final long[] array6 = new long[n];
            shiftUp(array4, 0, array6, 0, n, 4);
            final long[] ints = longArray2.m_ints;
            final int n7 = n4 << 3;
            final long[] array7 = new long[n7];
            n = 0;
            int n8;
            while (true) {
                n8 = n7;
                if (n >= n2) {
                    break;
                }
                long n9 = ints[n];
                int n10 = n;
                while (true) {
                    final int n11 = (int)n9;
                    final long n12 = n9 >>> 4;
                    addBoth(array7, n10, array4, array2[n11 & 0xF], array6, array2[(int)n12 & 0xF], n5);
                    n9 = n12 >>> 4;
                    if (n9 == 0L) {
                        break;
                    }
                    n10 += n4;
                }
                ++n;
            }
            while (true) {
                n8 -= n4;
                if (n8 == 0) {
                    break;
                }
                addShiftedUp(array7, n8 - n4, array7, n8, n4, 8);
            }
            return new LongArray(array7, 0, n4);
        }
        final long n13 = longArray2.m_ints[0];
        if (n13 == 1L) {
            return longArray;
        }
        final long[] array8 = new long[n4];
        multiplyWord(n13, longArray.m_ints, n3, array8, 0);
        return new LongArray(array8, 0, n4);
    }
    
    public void reduce(int reduceInPlace, final int[] array) {
        final long[] ints = this.m_ints;
        reduceInPlace = reduceInPlace(ints, 0, ints.length, reduceInPlace, array);
        if (reduceInPlace < ints.length) {
            System.arraycopy(ints, 0, this.m_ints = new long[reduceInPlace], 0, reduceInPlace);
        }
    }
    
    public LongArray square(int i, final int[] array) {
        i = this.getUsedLength();
        if (i == 0) {
            return this;
        }
        int n;
        long[] array2;
        long n2;
        int n3;
        for (n = i << 1, array2 = new long[n], i = 0; i < n; i = n3 + 1, array2[n3] = interleave2_32to64((int)(n2 >>> 32))) {
            n2 = this.m_ints[i >>> 1];
            n3 = i + 1;
            array2[i] = interleave2_32to64((int)n2);
        }
        return new LongArray(array2, 0, n);
    }
    
    public boolean testBitZero() {
        final long[] ints = this.m_ints;
        final int length = ints.length;
        boolean b = false;
        if (length > 0) {
            b = b;
            if ((ints[0] & 0x1L) != 0x0L) {
                b = true;
            }
        }
        return b;
    }
    
    public BigInteger toBigInteger() {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return ECConstants.ZERO;
        }
        final long[] ints = this.m_ints;
        final int n = usedLength - 1;
        final long n2 = ints[n];
        final byte[] array = new byte[8];
        final int n3 = 0;
        int i = 7;
        int n4 = 0;
        int n5 = 0;
        while (i >= 0) {
            final byte b = (byte)(n2 >>> i * 8);
            int n6 = 0;
            Label_0090: {
                if (n4 == 0) {
                    n6 = n5;
                    if (b == 0) {
                        break Label_0090;
                    }
                }
                array[n5] = b;
                n6 = n5 + 1;
                n4 = 1;
            }
            --i;
            n5 = n6;
        }
        final byte[] array2 = new byte[n * 8 + n5];
        for (int j = n3; j < n5; ++j) {
            array2[j] = array[j];
        }
        final int n7 = usedLength - 2;
        int n8 = n5;
        for (int k = n7; k >= 0; --k) {
            final long n9 = this.m_ints[k];
            for (int l = 7; l >= 0; --l, ++n8) {
                array2[n8] = (byte)(n9 >>> l * 8);
            }
        }
        return new BigInteger(1, array2);
    }
    
    @Override
    public String toString() {
        final int usedLength = this.getUsedLength();
        if (usedLength == 0) {
            return "0";
        }
        final long[] ints = this.m_ints;
        int n = usedLength - 1;
        final StringBuffer sb = new StringBuffer(Long.toBinaryString(ints[n]));
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final String binaryString = Long.toBinaryString(this.m_ints[n]);
            final int length = binaryString.length();
            if (length < 64) {
                sb.append("0000000000000000000000000000000000000000000000000000000000000000".substring(length));
            }
            sb.append(binaryString);
        }
        return sb.toString();
    }
}
