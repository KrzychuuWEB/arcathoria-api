package com.arcathoria.combat;

import com.arcathoria.combat.vo.Participant;

interface DamageCalculator {

    double calculate(final Participant participant);
}
