import { IProject } from 'app/shared/model/project.model';
import { IIntegration } from 'app/shared/model/integration.model';

export interface ICountry {
    id?: number;
    name?: string;
    code?: string;
    projects?: IProject[];
    integrations?: IIntegration[];
}

export class Country implements ICountry {
    constructor(
        public id?: number,
        public name?: string,
        public code?: string,
        public projects?: IProject[],
        public integrations?: IIntegration[]
    ) {}
}
