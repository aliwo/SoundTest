package audible.idelisteners;

import audible.LivePluginUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.util.ArrayList;
import java.util.List;

public class EditorModification implements Restartable {
    private final List<String> actionsToWrap;
    private final Listener listener;

    public EditorModification(Listener listener) {
        this.listener = listener;

        actionsToWrap = new ArrayList<String>();
        actionsToWrap.add("EditorCompleteStatement");
        actionsToWrap.add("EditorDeleteToWordStart");
        actionsToWrap.add("EditorDeleteToWordEnd");
        actionsToWrap.add("EditorStartNewLine");
        actionsToWrap.add("EditorDeleteLine");
        actionsToWrap.add("HippieCompletion");
        actionsToWrap.add("HippieBackwardCompletion");
        actionsToWrap.add("CommentByLineComment");
        actionsToWrap.add("CommentByBlockComment");
        actionsToWrap.add("$Undo");
        actionsToWrap.add("$Redo");
        actionsToWrap.add("ExpandAllRegions");
        actionsToWrap.add("CollapseAllRegions");
        //actionsToWrap.add("EditorBackSpace"); // TODO fails
        //actionsToWrap.add("ExpandLiveTemplateByTab"); // TODO fails
        //actionsToWrap.add("ExpandLiveTemplateCustom"); // TODO fails
        //actionsToWrap.add("EditorPaste"); // TODO fails
    }

    @Override
    public void start() {
        for (final String actionId : actionsToWrap) {
            LivePluginUtil.wrapAction(actionId, new LivePluginUtil.ActionWrapper() {
                @Override
                public void call(AnActionEvent event, AnAction action) {
                    listener.onEditorModification(actionId);
                    action.actionPerformed(event);
                }
            });
        }
    }

    @Override
    public void stop() {
        for (String actionId : actionsToWrap) {
            LivePluginUtil.unwrapAction(actionId);
        }
    }

    public interface Listener {
        void onEditorModification(String actionId);
    }
}