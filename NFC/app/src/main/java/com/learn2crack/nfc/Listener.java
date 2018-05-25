/**
 * @Unit_name        : Listener
 * @date_created     : 22 March, 2018
 * @author           : Shaurya Gomber
 * @last_update      : 22 March, 2018
 * @functions        : void onDialogDisplayed()
 *                     void onDIalogDismissed()
 * @synopsis         : Helper module to read NFC
 */

package com.learn2crack.nfc;
public interface Listener {
    void onDialogDisplayed();
    void onDialogDismissed();
}
