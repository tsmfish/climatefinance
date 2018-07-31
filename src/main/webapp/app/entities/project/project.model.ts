import { BaseEntity } from './../../shared';

export const enum ProjectType {
    'CCA',
    'CCM',
    'DRR',
    'DRM',
    'ENABLING'
}

export const enum FundingBasis {
    'NATIONAL',
    'REGIONAL'
}

export const enum Currency {
    'USD',
    'EUR',
    'NZD',
    'AUD',
    'XPF',
    'SBD',
    'VT',
    'FJD',
    'JPY'
}

export const enum Laterality {
    'BILATERAL',
    'MULTILATERAL',
    'OTHER'
}

export const enum Status {
    'PIPELINE',
    'CURRENT',
    'COMPLETED'
}

export const enum Modality {
    'ON_BUDGET',
    'OFF_BUDGET'
}

export class Project implements BaseEntity {
    constructor(
        public id?: number,
        public projectTitle?: string,
        public projectDescription?: any,
        public projectType?: ProjectType,
        public fundingBasis?: FundingBasis,
        public totalFundingAmount?: number,
        public totalFundingCurrency?: Currency,
        public estimatedCountryAllocation?: number,
        public timeFrame?: string,
        public principalSource?: string,
        public additionalSource?: string,
        public ministry?: string,
        public otherStakeholders?: string,
        public laterality?: Laterality,
        public appropriated?: boolean,
        public weightingPercentage?: string,
        public inkindPercentage?: string,
        public climateChangeAdaptation?: number,
        public climateChangeMitigation?: number,
        public disasterRiskReduction?: number,
        public disasterRiskMitigation?: number,
        public total?: number,
        public status?: Status,
        public modality?: Modality,
        public startYear?: any,
        public endYear?: any,
        public active?: boolean,
        public notes?: any,
        public country?: BaseEntity,
        public sector?: BaseEntity,
        public detailedSector?: BaseEntity,
        public disbursements?: BaseEntity[],
    ) {
        this.appropriated = false;
        this.active = false;
    }
}
