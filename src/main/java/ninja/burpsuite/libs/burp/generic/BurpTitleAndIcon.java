// Released under AGPL see LICENSE for more information
// Developed by Soroush Dalili (@irsdl)

package ninja.burpsuite.libs.burp.generic;

import ninja.burpsuite.libs.generic.ImageHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;

public class BurpTitleAndIcon {
    public static void resetTitle(BurpExtensionSharedParameters sharedParameters) {
        setTitle(sharedParameters, sharedParameters.get_originalBurpTitle());
    }

    public static void resetIcon(BurpExtensionSharedParameters sharedParameters) {
        setIcon(sharedParameters, sharedParameters.get_originalBurpIcon());
        removeMainFrameWindowFocusListener(sharedParameters);
    }

    public static void changeTitleAndIcon_noUiLock(BurpExtensionSharedParameters sharedParameters, String title, Image img) {
        setTitle_noUiLock(sharedParameters, title);
        setIcon_noUiLock(sharedParameters, img);
    }

    public static void setTitle(BurpExtensionSharedParameters sharedParameters, String title) {
        sharedParameters.get_mainFrameUsingMontoya().setTitle(title);
        sharedParameters.printDebugMessage("Burp Suite title was changed to: " + title);
    }

    public static void setTitle_noUiLock(BurpExtensionSharedParameters sharedParameters, String title) {
        SwingUtilities.invokeLater(() -> {
            setTitle(sharedParameters, title);
        });
    }

    private static void setIcon(BurpExtensionSharedParameters sharedParameters, Image img) {
        if (img != null) {
            java.util.List<Image> iconList = new ArrayList<>();
            iconList.add(img);
            for (Window window : Window.getWindows()) {
                window.setIconImages(iconList);
            }
                /* another way?
                    for (Frame frame : Frame.getFrames()) {
                        frame.setIconImages(iconList);
                    }
                    sharedParameters.get_mainFrame().setIconImage(img);
                */
            sharedParameters.printDebugMessage("Burp Suite icon has been updated");
        }
    }

    private static void setIcon_noUiLock(BurpExtensionSharedParameters sharedParameters, Image img) {
        SwingUtilities.invokeLater(() -> {
            setIcon(sharedParameters, img);
        });
    }

    private static void removeMainFrameWindowFocusListener(BurpExtensionSharedParameters sharedParameters) {
        if (sharedParameters.addedIconListener) {
            sharedParameters.addedIconListener = false;
            int listenerCount = sharedParameters.get_mainFrameUsingMontoya().getWindowFocusListeners().length;
            if (listenerCount > 0) {
                // We assume that the last one is ours!
                sharedParameters.get_mainFrameUsingMontoya().removeWindowFocusListener(sharedParameters.get_mainFrameUsingMontoya().getWindowFocusListeners()[listenerCount - 1]);
            }
        }
    }

    public static void setIcon(BurpExtensionSharedParameters sharedParameters, String imgPath, int iconSize, boolean isResource) {
        Image loadedImg;
        if (isResource) {
            loadedImg = ImageHelper.scaleImageToWidth(ImageHelper.loadImageResource(sharedParameters.extensionClass, imgPath), iconSize);
        } else {
            loadedImg = ImageHelper.scaleImageToWidth(ImageHelper.loadImageFile(imgPath), iconSize);
        }

        if (loadedImg != null) {
            setIcon_noUiLock(sharedParameters, loadedImg);

            if (sharedParameters.addedIconListener) {
                removeMainFrameWindowFocusListener(sharedParameters);
            }

            WindowFocusListener mainFrameWindowFocusListener = new WindowFocusListener() {

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    setIcon_noUiLock(sharedParameters, loadedImg);
                }

                @Override
                public void windowLostFocus(WindowEvent e) {
                    setIcon_noUiLock(sharedParameters, loadedImg);
                }
            };

            sharedParameters.get_mainFrameUsingMontoya().addWindowFocusListener(mainFrameWindowFocusListener);
            sharedParameters.addedIconListener = true;

        } else {
            sharedParameters.printlnError("Image could not be loaded to be used as the Burp Suite icon: " + imgPath);
        }
    }
}
