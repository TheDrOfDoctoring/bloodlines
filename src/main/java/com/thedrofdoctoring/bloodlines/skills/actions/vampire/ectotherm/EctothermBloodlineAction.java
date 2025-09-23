package com.thedrofdoctoring.bloodlines.skills.actions.vampire.ectotherm;

import com.thedrofdoctoring.bloodlines.capabilities.bloodlines.IBloodline;
import com.thedrofdoctoring.bloodlines.core.bloodline.BloodlineRegistry;
import com.thedrofdoctoring.bloodlines.skills.actions.vampire.DefaultVampireBloodlineAction;

public abstract class EctothermBloodlineAction extends DefaultVampireBloodlineAction {
    @Override
    protected IBloodline getBloodlineType() {
        return BloodlineRegistry.BLOODLINE_ECTOTHERM.get();
    }
}
