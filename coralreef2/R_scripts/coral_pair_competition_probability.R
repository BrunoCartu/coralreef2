# Carturan_et_al_2020_A_spatially_explicit_and_mechanistic_model_for_exploring_coral_reef_dynamics
# author: Bruno Carturan

# Goal: 
## To generate the coral_pair_competition_probability_simulation_community_NUMBER.csv when a new community is being simulated.
## The files contain probabilities of winning, losing and stand out interactions between two species, values are taken from Precoda et al's (2017) species-pair interaction probability dataset: 
### coralreef2/Dataset_original
## The coral_pair_competition_probability_simulation_community_NUMBER.csv files are exported in coralreef2/data during the initiallization of the virtual reef
## The script is called only if the csv file does not exist already.
## The procedure only works when launching simulations directly via Repast; it does not work when using R.

# Figures S1 to S12: same scipt as BRI_responseTraits_associations_Maveraging.R (in Modeling/coralreef2/R_scripts and /Modeling/R scripts and /Functional Traits/Traits analysis/Traits/Rscripts) 
# Figures S13 to S16: same scipt as Bleaching_logistic_response_DHW_Eakin_2010.R (in Modeling/coralreef2/R_scripts and Modeling/R scripts)

## From:
### HSV_summary.csv                          : summary of the explanatory variables to vary in the different steps of the HSV; created from HSV_summary.xls, which I manually created
### Traits_compilation_zooxanthellate_cut.csv: the dataset of the combined trait values (798 zooxzanthellate species, 22 traits); created in Traits_and_imputation/Rscripts/phylogeny_coral_and_functional_traits.R
### functionalTraitDF_model.csv              : the dataset for the 798 coral species and six functional groups of algae used in the model to input trait values; created in Traits_and_imputation/Rscripts/functionalDiversityTable_Model.R and exported in Simulations/Datasets_for_model and in coralreef2/coralreef2/data
### functionTrait_DS_Imputed.csv             : imputed coral traits dataset for 789 species and 11 traits; created in Traits_and_imputation/Rscripts/trait_data_compilation.R
### life_history_strategy.csv                : the competitive, stress-tolerant, weedy and generalist classification; created in Traits_and_imputation/Rscripts/life_history_strategy.R
### Initial_benthic_composition.csv          : the intial composition of the communities (for calibration with the three Caribbean sites and the ones for the HSV); created in Simulations/Rscripts/Hierarchically_structured_validation_Input_dataset.R
### coral_pair_competition_probability.csv   : curated Precoda et al's (2017) species-pair interaction probability dataset; created in Traits_and_imputation/Rscripts/competition_corals.R



createOutcomeCompetition <- function(workingDirectory,communtyNb){
  wd <- workingDirectory
  wd_Datasets_original <- paste(wd,"/datasets_original",sep="") # orginal dataset imported where full coral_pair_competition_probability.csv from ()
  # wd_Datasets_R <- paste(wd,"/datasets_R",sep="")               # datasets and results obtained from work on original datasets in R, but not used for the Repast model
  # wd_Datasets_outputs <- paste(wd,"/datasets_outputs",sep="")
  wd_Datasets_inputs <- paste(wd,"/data",sep="")                # folder where to export the subset of coral_pair_competition_probability.csv
  # wd_Figures <- paste(wd,"/figures",sep="")
  # wd_R_scripts <- paste(wd,"/R_scripts",sep="")
  # wd_outputs_simulation <- paste(wd,"/outputs_simulations",sep="")
  i <- communtyNb
  nameCoralSpeciesCSVPresent <- paste(wd_Datasets_inputs,paste("Species_present_in_simulation_community_",i,".csv",sep=""),sep="/")
  coralSpeciesCSVPresent <- file.exists(nameCoralSpeciesCSVPresent)
  # check if coral_pair_competition_probability_simulation_community_?.csv is already present
  nameCoral_pair_competition_proba <- paste(wd_Datasets_inputs,paste("coral_pair_competition_probability_simulation_community_",i,".csv",sep=""),sep="/")
  nameCoral_pair_competition_probaPresent <-  file.exists(nameCoral_pair_competition_proba)
  if(coralSpeciesCSVPresent && !nameCoral_pair_competition_probaPresent){
    ### Import the species present in the simulation
    speciesPresent <- read.csv(nameCoralSpeciesCSVPresent,header=T,stringsAsFactors = F)
    speciesPresent$species_names <- gsub("_"," ",speciesPresent$species_names) # replace "_" by " "
    ### Import output species-pair interction probability of Precoda_et_al_2017 (with updated names)
    speciesPairInteraction_original <- read.csv(paste(wd_Datasets_original,"coral_pair_competition_probability.csv",sep="/"),header=T,stringsAsFactors = F)
    length(speciesPairInteraction_original$species1) # 274911
    length(levels(as.factor(speciesPairInteraction_original$species1))) # 741
    ### number species in common in the 2 lists:
    speciesPresentInBothLists <- levels(as.factor(speciesPairInteraction_original$species1[speciesPairInteraction_original$species1 %in% speciesPresent$species_names]))
    length(speciesPresentInBothLists) # 10
    ### Make a subste of speciesPairInteraction_original with only the species present
    speciesPairInteraction <- speciesPairInteraction_original[speciesPairInteraction_original$species1 %in% speciesPresent$species_names,]
    length(speciesPairInteraction$species1) # 2220
    speciesPairInteraction <- speciesPairInteraction[speciesPairInteraction$species2 %in% speciesPresent$species_names,]
    length(speciesPairInteraction$species1) # 55 = (10*9)/2+10
    # remove species_a vs species_a raws
    speciesPairInteraction <- speciesPairInteraction[speciesPairInteraction$species1 != speciesPairInteraction$species2,]
    length(speciesPairInteraction$species1) # 45 = 55 - 10
    ### change " " to "_" in the names
    speciesPairInteraction$species1 <- gsub(" ","_",speciesPairInteraction$species1)
    speciesPairInteraction$species2 <- gsub(" ","_",speciesPairInteraction$species2)
    ### write csv, only the 1st 3 columns
    write.csv(speciesPairInteraction[,c(1,2,5)],nameCoral_pair_competition_proba,row.names = F)
  }
}
