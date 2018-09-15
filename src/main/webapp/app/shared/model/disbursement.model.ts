import { IProject } from 'app/shared/model/project.model';

export interface IDisbursement {
    id?: number;
    year?: number;
    amount?: number;
    project?: IProject;
}

export class Disbursement implements IDisbursement {
    constructor(public id?: number, public year?: number, public amount?: number, public project?: IProject) {}
}
