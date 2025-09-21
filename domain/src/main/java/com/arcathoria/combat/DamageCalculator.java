package com.arcathoria.combat;

import com.arcathoria.combat.vo.Damage;

interface DamageCalculator {

    Damage calculate(final Participant participant);
}
