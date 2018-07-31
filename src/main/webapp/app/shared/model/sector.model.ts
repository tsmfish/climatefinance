import { IProject } from 'app/shared/model/project.model';

export interface ISector {
    id?: number;
    name?: string;
    projects?: IProject[];
}

export class Sector implements ISector {
    constructor(public id?: number, public name?: string, public projects?: IProject[]) {}
}
