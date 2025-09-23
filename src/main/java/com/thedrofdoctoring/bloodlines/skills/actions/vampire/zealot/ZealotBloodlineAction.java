package com.thedrofdoctoring.bloodlines.skills.actions.vampire.zealot;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.actions.vampire.DefaultVampireBloodlineAction;

public abstract class ZealotBloodlineAction extends DefaultVampireBloodlineAction {
    @Override
    protected IBloodline getBloodlineType() {
        return BloodlineRegistry.BLOODLINE_ZEALOT.get();
    }
}
