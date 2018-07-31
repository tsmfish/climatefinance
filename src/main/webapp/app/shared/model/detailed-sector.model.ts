import { IProject } from 'app/shared/model/project.model';

export interface IDetailedSector {
    id?: number;
    name?: string;
    projects?: IProject[];
}

export class DetailedSector implements IDetailedSector {
    constructor(public id?: number, public name?: string, public projects?: IProject[]) {}
}
