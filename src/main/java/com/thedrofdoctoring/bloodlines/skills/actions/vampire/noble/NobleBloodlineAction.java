package com.thedrofdoctoring.bloodlines.skills.actions.vampire.noble;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.actions.vampire.DefaultVampireBloodlineAction;

public abstract class NobleBloodlineAction extends DefaultVampireBloodlineAction {
    @Override
    protected IBloodline getBloodlineType() {
        return BloodlineRegistry.BLOODLINE_NOBLE.get();
    }
}
