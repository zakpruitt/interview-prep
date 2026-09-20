<#
.SYNOPSIS
    Small wrapper around the interview-prep workflow.

.EXAMPLE
    .\prep.ps1 new two-sum            # scaffold solution + test from leetcode.com
    .\prep.ps1 new https://leetcode.com/problems/valid-sudoku/
    .\prep.ps1 test                   # run every test
    .\prep.ps1 test TwoSum            # run just TwoSumTest
    .\prep.ps1 list                   # list the problems already in the repo
    .\prep.ps1 build                  # compile everything
#>
param(
    [Parameter(Position = 0)]
    [ValidateSet('new', 'test', 'list', 'build', 'clean')]
    [string]$Command = 'list',

    [Parameter(Position = 1, ValueFromRemainingArguments = $true)]
    [string[]]$Args
)

$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot

switch ($Command) {
    'new' {
        if (-not $Args) { throw "Usage: .\prep.ps1 new <slug-or-url>" }
        & python (Join-Path $root 'tools\leetcode.py') @Args
    }
    'test' {
        if ($Args) {
            & mvn -f (Join-Path $root 'pom.xml') test "-Dtest=$($Args[0])Test" '-DfailIfNoTests=false'
        } else {
            & mvn -f (Join-Path $root 'pom.xml') test
        }
    }
    'build' { & mvn -f (Join-Path $root 'pom.xml') -q compile; if ($?) { Write-Output 'compiles clean' } }
    'clean' { & mvn -f (Join-Path $root 'pom.xml') -q clean }
    'list' {
        Get-ChildItem (Join-Path $root 'src\main\java\leetcode\*.java') |
            Where-Object { $_.BaseName -notin @('ListNode', 'TreeNode') } |
            ForEach-Object {
                $hasTest = Test-Path (Join-Path $root "src\test\java\leetcode\$($_.BaseName)Test.java")
                [pscustomobject]@{ Problem = $_.BaseName; Test = if ($hasTest) { 'yes' } else { '-' } }
            } | Format-Table -AutoSize
    }
}
