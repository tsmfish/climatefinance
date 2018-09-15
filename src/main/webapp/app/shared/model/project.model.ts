import { Moment } from 'moment';
import { ICountry } from 'app/shared/model/country.model';
import { ISector } from 'app/shared/model/sector.model';
import { IDetailedSector } from 'app/shared/model/detailed-sector.model';
import { IDisbursement } from 'app/shared/model/disbursement.model';

export const enum ProjectType {
    CCA = 'CCA',
    CCM = 'CCM',
    DRR = 'DRR',
    DRM = 'DRM',
    ENABLING = 'ENABLING'
}

export const enum FundingBasis {
    NATIONAL = 'NATIONAL',
    REGIONAL = 'REGIONAL'
}

export const enum Currency {
    USD = 'USD',
    EUR = 'EUR',
    NZD = 'NZD',
    AUD = 'AUD',
    XPF = 'XPF',
    SBD = 'SBD',
    VT = 'VT',
    FJD = 'FJD',
    JPY = 'JPY'
}

export const enum Laterality {
    BILATERAL = 'BILATERAL',
    MULTILATERAL = 'MULTILATERAL',
    OTHER = 'OTHER'
}

export const enum Status {
    PIPELINE = 'PIPELINE',
    CURRENT = 'CURRENT',
    COMPLETED = 'COMPLETED'
}

export const enum Modality {
    ON_BUDGET = 'ON_BUDGET',
    OFF_BUDGET = 'OFF_BUDGET'
}

export interface IProject {
    id?: number;
    projectTitle?: string;
    projectDescription?: any;
    projectType?: ProjectType;
    fundingBasis?: FundingBasis;
    totalFundingAmount?: number;
    totalFundingCurrency?: Currency;
    estimatedCountryAllocation?: number;
    timeFrame?: string;
    principalSource?: string;
    additionalSource?: string;
    ministry?: string;
    otherStakeholders?: string;
    laterality?: Laterality;
    appropriated?: boolean;
    weightingPercentage?: string;
    inkindPercentage?: string;
    climateChangeAdaptation?: number;
    climateChangeMitigation?: number;
    disasterRiskReduction?: number;
    disasterRiskMitigation?: number;
    total?: number;
    status?: Status;
    modality?: Modality;
    startYear?: Moment;
    endYear?: Moment;
    active?: boolean;
    notes?: any;
    country?: ICountry;
    sector?: ISector;
    detailedSector?: IDetailedSector;
    disbursements?: IDisbursement[];
}

export class Project implements IProject {
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
        public startYear?: Moment,
        public endYear?: Moment,
        public active?: boolean,
        public notes?: any,
        public country?: ICountry,
        public sector?: ISector,
        public detailedSector?: IDetailedSector,
        public disbursements?: IDisbursement[]
    ) {
        this.appropriated = false;
        this.active = false;
    }
}
