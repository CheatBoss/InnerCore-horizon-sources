package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.launcher.*;
import com.zhekasmirnov.horizon.modloader.*;
import android.app.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import com.zhekasmirnov.horizon.activity.main.adapter.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;
import com.zhekasmirnov.horizon.activity.util.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import android.os.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.content.*;
import android.annotation.*;
import com.zhekasmirnov.horizon.*;

public class HorizonActivity extends AppCompatActivity
{
    private static PackHolder pendingPackHolder;
    private PackHolder packHolder;
    private ContextHolder contextHolder;
    private TaskManager taskManager;
    private ModContext modContext;
    private MenuHolder menuHolder;
    private boolean isLaunching;
    
    public HorizonActivity() {
        this.isLaunching = false;
    }
    
    public static void prepareForLaunch(final PackHolder packHolder) {
        HorizonActivity.pendingPackHolder = packHolder;
    }
    
    private void initializeContext() {
        if (this.packHolder != null) {
            this.packHolder.deselectAndUnload();
        }
        this.packHolder = HorizonActivity.pendingPackHolder;
        if (this.packHolder == null) {
            throw new RuntimeException("HorizonActivity launched without pack holder");
        }
        this.contextHolder = this.packHolder.getContextHolder();
        this.modContext = this.packHolder.getModContext();
        this.taskManager = this.contextHolder.getTaskManager();
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        this.initializeContext();
        super.onCreate(savedInstanceState);
        HorizonApplication.moveToBackgroundIfNotOnTop((Activity)this);
        this.setContentView(2131427358);
        final View launchLayout = this.findViewById(2131230825);
        final View originalLaunchButton = this.findViewById(2131230855);
        this.packHolder.getPack().buildCustomMenuLayout(launchLayout, originalLaunchButton);
        View launchButton = this.findViewById(2131230855);
        if (launchButton == null) {
            launchButton = launchLayout.findViewById(0);
        }
        if (launchButton != null) {
            launchButton.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                public void onClick(final View view) {
                    if (!HorizonActivity.this.isLaunching) {
                        HorizonActivity.this.isLaunching = true;
                        HorizonActivity.this.taskManager.addTask(new TaskSequence.AnonymousTask() {
                            @Override
                            public void run() {
                                HorizonActivity.this.packHolder.prepareForLaunch();
                                HorizonActivity.this.packHolder.getPack().launch((Activity)HorizonActivity.this, null, new Runnable() {
                                    @Override
                                    public void run() {
                                        HorizonActivity.this.isLaunching = false;
                                        HorizonActivity.this.sendEventPackLaunched();
                                    }
                                });
                            }
                            
                            @Override
                            public String getDescription() {
                                return "preparing for launch";
                            }
                        });
                    }
                }
            });
        }
        else {
            Toast.makeText((Context)this, (CharSequence)"failed to find launch button in modified layout, please set it id to zero", 1).show();
        }
        final ProgressBarHolder progressBarHolder = new ProgressBarHolder((Activity)this, this.findViewById(2131230858), this.findViewById(2131230859));
        this.taskManager.addStateCallback(progressBarHolder);
        (this.menuHolder = new MenuHolder((Activity)this, this.findViewById(2131230862))).addAndAttachEntryBuilder(this.taskManager, new MenuEntryBuilder() {
            @Override
            protected boolean prepare(final List<MenuEntry> entries) {
                entries.add(new MenuEntry() {
                    @Override
                    protected int getLayoutId() {
                        return 2131427390;
                    }
                    
                    private View createCategoryView(final ViewGroup layout, final String text, final Collection<Bitmap> icon) {
                        final LayoutInflater inflater = HorizonActivity.this.getLayoutInflater();
                        final View categoryView = inflater.inflate(2131427386, layout, false);
                        final ImageView iconView = (ImageView)categoryView.findViewById(2131230852);
                        final TextView titleView = (TextView)categoryView.findViewById(2131230854);
                        titleView.setText((CharSequence)text);
                        if (icon != null && icon.size() > 0) {
                            iconView.setImageDrawable((Drawable)new AnimatedBitmapCollectionDrawable(icon, 4000, 400));
                        }
                        layout.addView(categoryView, layout.getChildCount());
                        return categoryView;
                    }
                    
                    private View createCategoryView(final ViewGroup layout, final int text, final int icon) {
                        final View categoryView = this.createCategoryView(layout, HorizonActivity.this.getResources().getString(text), null);
                        final ImageView iconView = (ImageView)categoryView.findViewById(2131230852);
                        iconView.setImageResource(icon);
                        return categoryView;
                    }
                    
                    private View createCategoryView(final ViewGroup layout, final Pack.MenuActivityFactory factory) {
                        Collection<Bitmap> icon = factory.getIconGraphicsBitmaps();
                        if (icon == null) {
                            final String iconName = factory.getIconGraphics();
                            if (iconName != null) {
                                icon = HorizonActivity.this.packHolder.getGraphics().getGroup(iconName);
                            }
                        }
                        final View categoryView = this.createCategoryView(layout, "" + factory.getMenuTitle(), icon);
                        categoryView.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                            public void onClick(final View v) {
                                if (!HorizonActivity.this.isLaunching) {
                                    CustomMenuActivity.prepareForStart(factory);
                                    HorizonActivity.this.startActivity(new Intent((Context)HorizonActivity.this, (Class)CustomMenuActivity.class));
                                }
                            }
                        });
                        return categoryView;
                    }
                    
                    @Override
                    public void bind(final View view) {
                        final Pack pack = HorizonActivity.this.packHolder.getPack();
                        final LinearLayout layout = (LinearLayout)view.findViewById(2131230851);
                        layout.removeAllViews();
                        final PackManifest manifest = HorizonActivity.this.packHolder.getManifest();
                        final View.OnClickListener onClickListener = (View.OnClickListener)new View.OnClickListener() {
                            public void onClick(final View v) {
                                HorizonActivity.this.taskManager.addTask(new TaskSequence.AnonymousTask() {
                                    @Override
                                    public void run() {
                                        HorizonActivity.this.packHolder.showDialogWithPackInfo((Activity)HorizonActivity.this, true);
                                    }
                                });
                            }
                        };
                        final TextView packTitleView = (TextView)view.findViewById(2131230857);
                        final TextView packDescriptionView = (TextView)view.findViewById(2131230856);
                        packTitleView.setText((CharSequence)manifest.pack);
                        packDescriptionView.setText((CharSequence)manifest.description);
                        packTitleView.setOnClickListener(onClickListener);
                        packDescriptionView.setOnClickListener(onClickListener);
                        this.createCategoryView((ViewGroup)layout, 2131624042, 2131165313).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
                            public void onClick(final View v) {
                                if (!HorizonActivity.this.isLaunching) {
                                    ModsActivity.prepareForStart(HorizonActivity.this.packHolder);
                                    HorizonActivity.this.startActivity(new Intent((Context)HorizonActivity.this, (Class)ModsActivity.class));
                                }
                            }
                        });
                        final List<Pack.MenuActivityFactory> activityFactories = pack.getMenuActivityFactories();
                        for (final Pack.MenuActivityFactory activityFactory : activityFactories) {
                            this.createCategoryView((ViewGroup)layout, activityFactory);
                        }
                    }
                    
                    @Override
                    public boolean isDraggable() {
                        return false;
                    }
                    
                    @Override
                    public boolean isRemovable() {
                        return false;
                    }
                });
                return true;
            }
            
            @Override
            protected String getEntryName() {
                return "Main Menu";
            }
            
            @Override
            protected String getTaskDescription() {
                return "building menu";
            }
        });
        if (this.packHolder.isUpdateAvailable()) {
            this.menuHolder.addAndAttachEntryBuilder(this.taskManager, new MenuEntryBuilder() {
                @Override
                protected String getEntryName() {
                    return "Update";
                }
                
                @Override
                protected boolean prepare(final List<MenuEntry> entries) {
                    entries.add(new MenuEntry() {
                        @Override
                        public void bind(final View group) {
                        }
                        
                        @Override
                        public void onClick(final View v) {
                            HorizonActivity.this.taskManager.addTask(new TaskSequence.AnonymousTask() {
                                @Override
                                public void run() {
                                    HorizonActivity.this.packHolder.showDialogWithChangelog((Activity)HorizonActivity.this, new Runnable() {
                                        @Override
                                        public void run() {
                                            HorizonActivity.this.taskManager.addTask(new TaskSequence.AnonymousTask() {
                                                @Override
                                                public void run() {
                                                    if (HorizonActivity.this.isLaunching) {
                                                        return;
                                                    }
                                                    HorizonActivity.this.isLaunching = true;
                                                    if (DialogHelper.awaitDecision((Activity)HorizonActivity.this, 2131624088, 2131624086, 2131624095, 17039360)) {
                                                        HorizonActivity.this.runOnUiThread((Runnable)new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                MenuEntry.this.destroy();
                                                            }
                                                        });
                                                        HorizonActivity.this.packHolder.update();
                                                        HorizonApplication.restart();
                                                    }
                                                }
                                                
                                                @Override
                                                public String getDescription() {
                                                    return "Updating pack...";
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                        
                        @Override
                        protected int getLayoutId() {
                            return 2131427391;
                        }
                    });
                    return true;
                }
            });
        }
        this.menuHolder.addEntryBuilder(new MenuEntryBuilder() {
            @Override
            protected boolean prepare(final List<MenuEntry> entries) {
                final LogFileHandler logHandler = LogFileHandler.getInstance();
                for (final File crash : logHandler.getAllFiles("crash.txt")) {
                    entries.add(new MenuEntry() {
                        private boolean archive = true;
                        
                        @Override
                        protected int getLayoutId() {
                            return 2131427387;
                        }
                        
                        @Override
                        public void onClick(final View v) {
                            this.archive = false;
                            this.destroy();
                            final Intent intent = new Intent((Context)HorizonActivity.this, (Class)CrashReportActivity.class);
                            intent.putExtra("crash_path", crash.getAbsolutePath());
                            HorizonActivity.this.startActivity(intent);
                        }
                        
                        @Override
                        public void onRemoved() {
                            if (this.archive) {
                                CrashReportActivity.archiveCrashReport(crash);
                                Toast.makeText((Context)HorizonActivity.this, (CharSequence)"Archived", 1).show();
                            }
                        }
                    });
                }
                return true;
            }
            
            @Override
            protected String getEntryName() {
                return "Crash";
            }
            
            @Override
            protected String getTaskDescription() {
                return "checking crash dump";
            }
        });
        this.menuHolder.attachBuilders(this.taskManager);
        if (this.modContext != null) {
            this.modContext.addEventReceiver("disabledDueToCrash", new ModContext.EventReceiver() {
                @Override
                public void onEvent(final Mod... mods) {
                    for (final Mod mod : mods) {
                        HorizonActivity.this.menuHolder.addAndAttachEntryBuilder(HorizonActivity.this.taskManager, new MenuEntryBuilder() {
                            @Override
                            protected boolean prepare(final List<MenuEntry> entries) {
                                entries.add(new MenuEntry() {
                                    @Override
                                    protected int getLayoutId() {
                                        return 2131427389;
                                    }
                                    
                                    @Override
                                    public void bind(final View view) {
                                        final TextView info = (TextView)view.findViewById(2131230913);
                                        String text = info.getText().toString();
                                        text = String.format(text, mod.getDisplayedName());
                                        info.setText((CharSequence)text);
                                    }
                                    
                                    @Override
                                    public void onClick(final View v) {
                                        this.destroy();
                                        final Mod.ConfigurationInterface config = mod.getConfigurationInterface();
                                        config.setActive(true);
                                    }
                                    
                                    @Override
                                    public void onRemoved() {
                                    }
                                });
                                return true;
                            }
                            
                            @Override
                            protected String getEntryName() {
                                return "Crash";
                            }
                            
                            @Override
                            protected String getTaskDescription() {
                                return "checking crash dump";
                            }
                        });
                    }
                }
            });
        }
        this.packHolder.getModList().startRefreshTask(null);
        final Drawable customDrawable = this.packHolder.getPack().getRandomCustomDrawable("menu-background");
        if (customDrawable != null) {
            final ImageView backgroundView = (ImageView)this.findViewById(2131230849);
            backgroundView.setImageDrawable(customDrawable);
        }
        else {
            this.taskManager.addTask(new TaskSequence.AnonymousTask() {
                @Override
                public void run() {
                    final PackGraphics graphics = HorizonActivity.this.packHolder.getGraphics();
                    final Collection<Bitmap> allBackgrounds = graphics.getGroup("background");
                    if (allBackgrounds != null) {
                        final AnimatedBitmapCollectionDrawable drawable = new AnimatedBitmapCollectionDrawable(allBackgrounds, 9000, 750);
                        drawable.setAnimationParameters(3.5E-4f, 50.0f, 25.0f, true);
                        HorizonActivity.this.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                final ImageView backgroundView = (ImageView)HorizonActivity.this.findViewById(2131230849);
                                backgroundView.setImageDrawable((Drawable)drawable);
                            }
                        });
                    }
                }
            });
        }
        new Handler().postDelayed((Runnable)new Runnable() {
            @Override
            public void run() {
                HorizonActivity.this.addNativeAdBannersToMenu();
            }
        }, 1000L);
    }
    
    private void addNativeAdBannersToMenu() {
        this.menuHolder.clearByName("Ads");
        do {
            AdsManager.getInstance().loadAd("native", 2000, new AdsManager.AdListener() {
                @Override
                public void onAdLoaded(final AdContainer container) {
                    HorizonActivity.this.menuHolder.addAndAttachEntryBuilder(HorizonActivity.this.taskManager, new MenuEntryBuilder() {
                        @Override
                        protected String getEntryName() {
                            return "Ads";
                        }
                        
                        @Override
                        protected boolean prepare(final List<MenuEntry> entries) {
                            entries.add(new MenuEntry() {
                                @Override
                                public void bind(final View group) {
                                    try {
                                        group.findViewById(2131230845).setId(65535);
                                        group.findViewById(2131230846).setId(65537);
                                        group.findViewById(2131230847).setId(65538);
                                        group.findViewById(2131230848).setId(65545);
                                        container.inflate((ViewGroup)group);
                                    }
                                    catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                                
                                @Override
                                protected int getLayoutId() {
                                    return 2131427384;
                                }
                            });
                            return true;
                        }
                    });
                }
            }, "horizon-dev", "pack-main-menu");
        } while (AdsManager.getInstance().runAdditionalBannerRandom());
    }
    
    public void onBackPressed() {
        if (this.isLaunching) {
            Toast.makeText((Context)this, (CharSequence)"Cannot exit while launching", 1).show();
            return;
        }
        if (this.packHolder.isPreparedForLaunch()) {
            Toast.makeText((Context)this, (CharSequence)"Cannot exit if pack is prepared for launch", 1).show();
            return;
        }
        this.taskManager.addTask(new TaskSequence.AnonymousTask() {
            @Override
            public void run() {
                if (DialogHelper.awaitDecision((Activity)HorizonActivity.this, 2131624013, 2131624111, 2131624014, 17039360)) {
                    HorizonActivity.this.packHolder.deselectAndUnload();
                    HorizonActivity.this.exitToPackSelector();
                }
            }
        });
    }
    
    private void exitToPackSelector() {
        this.runOnUiThread((Runnable)new Runnable() {
            @SuppressLint({ "ApplySharedPref" })
            @Override
            public void run() {
                final SharedPreferences preferences = HorizonActivity.this.getSharedPreferences("packs", 0);
                preferences.edit().putString("currently_selected_pack_path", (String)null).commit();
                final HorizonApplication application = HorizonApplication.getInstance();
                if (application != null && application.isActivityRunning(PackSelectorActivity.class)) {
                    HorizonActivity.access$801(HorizonActivity.this);
                    return;
                }
                HorizonActivity.this.startActivity(new Intent((Context)HorizonActivity.this, (Class)PackSelectorActivity.class));
                HorizonActivity.this.finish();
            }
        });
    }
    
    private void sendEventPackLaunched() {
        if (this.packHolder != null) {
            final PackManifest manifest = this.packHolder.getManifest();
            final Bundle bundle = new Bundle();
            bundle.putString("pack", (manifest != null) ? manifest.pack : "null");
            HorizonApplication.initializeFirebase((Context)this);
            HorizonApplication.sendFirebaseEvent("horizon_pack_launch", bundle);
        }
    }
    
    static /* synthetic */ void access$801(final HorizonActivity x0) {
        x0.onBackPressed();
    }
    
    static {
        HorizonLibrary.include();
        HorizonActivity.pendingPackHolder = null;
    }
}
