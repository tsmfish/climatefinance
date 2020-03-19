import { Pipe, PipeTransform } from '@angular/core';

type ProjectTypeTooltip = {
    title: string;
    text: string;
};

const PROJECT_TYPES: { [key: string]: ProjectTypeTooltip } = {
    CCA: {
        title: 'Climate Change Adaptation',
        text: `Activities that respond to the adverse impacts of climate change on
the environment, human wellbeing and survival, and culture – reducing
vulnerability or increasing capacity to make change (resilience). For
example coastal defences, food and water security, improving health
etc.`
    },
    CCM: {
        title: 'Climate Change Mitigation',
        text: `Activities that contribute to lowering the cause of climate change
(greenhouse gas emissions). For example, installation of renewable
energy sources, fuel efficiency, reducing energy use, carbon storage
in vegetation (REDD+), etc.`
    },

    DRM: {
        title: 'Disaster Risk Management',
        text: `Activities that respond to the damages and losses caused by a disaster
on humans, environment and infrastructure`
    },

    DRR: {
        title: 'Disaster Risk Reduction',
        text: `Activities that contribute to lowering the risks associated with
disasters, on humans, environment and infrastructure`
    },

    ENABLING: {
        title: 'Enabling',
        text: `This category indicates projects that target institutional and
capacity strengthening, creating a more effective enabling environment
for the delivery of climate change and disaster risk related
activities.`
    }
};

@Pipe({ name: 'projectType' })
export class ProjectTypePipe implements PipeTransform {
    transform(name: string) {
        console.log('Pipe called:', name);
        return PROJECT_TYPES[name] || { title: 'Unknown', text: '' };
    }
}
