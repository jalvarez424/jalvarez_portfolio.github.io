/*
	Video Game Sales Cleaning and Exploration Project (in millions)
*/

-- checking how many rows there are to see how the import went (looking for 16598 rows)
select count(*)
from vgsales_cleaned_original;

-- Data Cleaning steps
# 1. Remove Duplicates
# 2. Standardize the Data (issues like spelling is fixed)
# 3. Clean up Null values or blank values
# 4. Remove Any Columns that are unnecessary

# Let's create a new table to work on that is equal to the raw data (so we don't mess with that)
create table vgsales_staging
like vgsales_cleaned_original;

# Insert the items from raw data table to new table
insert vgsales_staging
select * from vgsales_cleaned_original;

# check the new table to see if insert worked
select * from vgsales_staging;

-- 1. Remove Duplicates - to remove duplicates, we're going to try including a row number to each row, partitioning by columns that would have probably have duplicate values
# If there are any values of row number >= 2, then that means there are duplicates (it will increment if the exact same row data is found again)
# If you want to check only exact duplicates, then partition over every column instead of only certain ones

with vgsales_duplicate_cte as
(
select *,
row_number() over(partition by `Name`, Platform, `Year`, Genre, Publisher) as row_num
from vgsales_staging
)
select *
from vgsales_duplicate_cte
where row_num > 1;

# we found two rows that met this criteria, let's double check if it's actually a dupe by checking the name

select * from vgsales_staging
where `Name` = "Madden NFL 13";

# in this case, Madden NFL 13 has two rows for the same Platform (PS3), and one of those rows looks like it has null or incomplete data
# so we will remove that row

select * from vgsales_staging
where `Name` = "Wii de Asobu: Metroid Prime";

# Wii de Asobu has two rows with the exact same data, so that's an automatic deletion

# There are different ways to do this, but in this case we'll just delete the rows directly

delete
from vgsales_staging
where `Rank` = 15002 and `Rank` = 16130;


-- 2. Standardizing data - finding issues within the data and fixing them

# Whitespace Trimming front and back on the text fields

select `Name`, trim(`Name`), Platform, trim(Platform), Genre, trim(Genre), Publisher, trim(Publisher)
from vgsales_staging;

update vgsales_staging
set `Name` = trim(`Name`), Platform = trim(Platform), Genre = trim(Genre), Publisher = trim(Publisher);

# check work - in this case, no rows were updated so they already had no whitespace


# Now we'll do some formatting to make sure the sales are all displaying up to 2 decimal places
/*
select NA_Sales, format(NA_Sales, 2),
	EU_Sales, format(EU_Sales, 2),
    JP_Sales, format(JP_sales, 2),
    Other_Sales, format(Other_Sales, 2),
    Global_Sales, format(Global_Sales, 2)
from vgsales_staging;
*/
/*
update vgsales_staging
set NA_Sales = format(NA_Sales, 2),
	EU_Sales = format(EU_Sales, 2),
    JP_Sales = format(JP_sales, 2),
    Other_Sales = format(Other_Sales, 2),
    Global_Sales = format(Global_Sales, 2);
*/    

# check unique Publishers
select distinct Publisher
from vgsales_staging;

-- 3. Check for NULL or blank values
# Note: had to turn off auto start code completion in edit->preferences to avoid MySQL Workbench hanging and freezing
select *
from vgsales_staging
where `Rank` is null or `Rank` = 0 or `Name` is null or `Name` = '' or Platform is null or Platform = ''
or `Year` is null or `Year` = '' or Genre is null or Genre = '' or Publisher is null or Publisher = ''
or NA_Sales is null or NA_Sales = '' or EU_Sales is null or EU_Sales = '' or JP_Sales is null or JP_Sales = ''
or Other_Sales is null or Other_Sales = '' or Global_Sales is null or Global_Sales = '';

select *
from vgsales_staging
where Other_Sales is null or Other_Sales = '';

select *
from vgsales_staging
where Global_Sales is null or Global_Sales = '';

# Found quite a few rows that have 0 for sales but that's ok, just means that country didn't get any sales for that game
# Checking Other and Global by themselves just in case, if Global sales is 0 that might make that row invalid for our data.




-- Now that Data Cleaning has been completed, we can do some Exploratory Analysis

# Let's select the data that we want to start with
select * from vgsales_staging;

# Total Sales Based on Genre (which Genre sold the most?)
select Genre, round(sum(Global_Sales), 2)
from vgsales_staging
group by Genre
order by 2 desc;

# from this query, we can see that the Action Genre sold the most globally, while Strategy games were sold the least (unfortunate)
# let's split it out now by continent
select Genre, round(sum(NA_Sales), 2), round(sum(EU_Sales), 2), round(sum(JP_Sales), 2)
from vgsales_staging
group by Genre
order by 2 desc, 3 desc, 4 desc;

# Very interesting! We can see just from a glance that the highest selling genre in North America and Europe was Action, but in Japan, it was Role-Playing!

# I would like to see now if our sum of world location sales matches up to Global sales
select Genre, round(sum(NA_Sales) + sum(EU_Sales) + sum(JP_Sales) + sum(Other_Sales), 2) as agg_sum, round(sum(Global_Sales), 2) as global_sales
from vgsales_staging
group by Genre;
# Awesome! The aggregate function shows that it is indeed matching up closely enough.

# Which are the top 5 games sold in the United States in the latest 3 years?
select * from vgsales_staging
where `year` between (select max(`year`) - 3 from vgsales_staging)
	and (select max(`year`) from vgsales_staging)
order by NA_Sales desc
limit 5;

# What is the average of Racing game sales in Japan published by Sony?
select round(avg(JP_Sales), 2) as average_jp_racing_sales from vgsales_staging
where Genre = "Racing" and Publisher = "Sony Computer Entertainment";

# How many Simulation games were sold in Europe in 2006?
select count(*)
from vgsales_staging
where Genre = "Simulation" and `year` = 2006
and EU_Sales != 0;

# Self join table to try and show games with sequels

#select v1.`Name`, v2.`Name`, v1.Platform
#from vgsales_staging v1
#join vgsales_staging v2
#	on v1.Platform = v2.Platform
#group by v1.`Name`, v2.`Name`, v1.Platform
#having v1.`Name` like concat(v2.`Name`, "%");

select v1.`Name`, v2.`Name`, v1.Platform
from vgsales_staging v1, vgsales_staging v2
where v1.`Name` like concat(v2.`Name`, "%");
