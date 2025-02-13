package me.mattyhd0.chatcolor.player;

import me.mattyhd0.chatcolor.pattern.api.BasePattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CPlayer {

    @Nullable
    private BasePattern basePattern;

    public CPlayer(final @Nullable BasePattern basePattern){
        this.basePattern = basePattern;
    }
    @Nullable
    public BasePattern getPattern() {
        return basePattern;
    }

    public void setPattern(final @NotNull BasePattern pattern){
        this.basePattern = pattern;
    }

    public void disablePattern(){
        this.basePattern = null;
    }


}
